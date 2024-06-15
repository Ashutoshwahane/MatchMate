package dev.ashutoshwahane.matchmate.domain.utils

import dev.ashutoshwahane.matchmate.utils.DataResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class ParameterUseCase<in Parameter, T> {

    abstract suspend fun run(params: Parameter, dataType: DataType): T
    private var job: Job? = null

    /**
     * @param [dataType] - will help us in kind of data we want fetch. we have to pass this to repository and take a decision there.
     * @param [scope] - useCase runs entire code inside this scope, we can use jobs and discuss the API at our will.
     * @param [params] - you can pass any kind of DataType inside and you will get it back in [run] implementation.
     */
    operator fun invoke(
        scope: CoroutineScope = CoroutineScope(IO),
        params: Parameter,
        dataType: DataType = DataType.FORCE_CACHE_STRATEGY,
        dataResourceValue: (DataResource<T>) -> Unit = {},
        callback: UseCaseResponse<T>? = null,
    ) {
        job = scope.launch {
            dataResourceValue.invoke(DataResource.loading())
            callback?.onLoading()
            try {
                val result = run(params, dataType)
                dataResourceValue.invoke(DataResource.success(result))
                callback?.onSuccess(result)
            } catch (throwable: Throwable) {
                dataResourceValue.invoke(DataResource.error(throwable.cause))
                callback?.onError(throwable.cause?.message.toString())
            }


        }
    }

    fun cancel() {
        job?.cancel()
    }

    fun isJobNull() = job == null
}