package com.abdulkadirkara.common.networkResponse

import retrofit2.HttpException
import java.io.IOException

/**
 * A sealed interface representing the different states of a network response.
 *
 * This interface is used to encapsulate the various possible outcomes of a network request.
 * It helps in handling success, loading, empty, and error states in a clean and type-safe manner.
 * It allows differentiating between successful, loading, empty, and error responses,
 * making the code more manageable and easier to test.
 */
sealed interface NetworkResponse<out T> {

    /**
     * Represents a successful network response containing data.
     *
     * @param T The type of the data returned in the response.
     * @property data The actual data returned from the network request.
     */
    data class Success<out T>(val data: T) : NetworkResponse<T>

    /**
     * Represents the loading state of the network request.
     *
     * This state is used to represent that the network request is currently in progress.
     * It doesn't carry any data.
     */
    data object Loading : NetworkResponse<Nothing>

    /**
     * Represents an empty response, typically used for cases where no content is returned.
     */
    data object Empty : NetworkResponse<Nothing>

    /**
     * A sealed interface representing errors that may occur during the network request.
     *
     * This interface encompasses all error states that can occur during a network request.
     */
    sealed interface Error : NetworkResponse<Nothing> {

        /**
         * The error message that describes the cause of the failure.
         */
        val message: String

        /**
         * Represents an HTTP error, typically for issues like 404 or 500 errors.
         *
         * @param exception The exception related to the HTTP error (e.g., `HttpException`).
         * @property message A descriptive error message.
         */
        data class HttpError(val exception: HttpException, override val message: String) : Error

        /**
         * Represents a network error, typically caused by connectivity issues (e.g., no internet).
         *
         * @param exception The exception related to the network error (e.g., `IOException`).
         * @property message A descriptive error message.
         */
        data class NetworkError(val exception: IOException, override val message: String) : Error

        /**
         * Represents an unknown error, typically caused by an unexpected exception.
         *
         * @param exception The exception related to the unknown error.
         * @property message A descriptive error message.
         */
        data class UnknownError(val exception: Throwable, override val message: String) : Error
    }
}

/**
 * Executes a given action if the network response is a success.
 *
 * This is a convenient extension function for handling successful responses. It allows
 * you to define a block of code that will only be executed if the response is a success.
 *
 * @param action The action to be executed if the response is a success.
 * @return The current network response.
 */
inline fun <T> NetworkResponse<T>.onSuccess(action: (T) -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Success) {
        action(data)
    }
    return this
}

/**
 * Executes a given action if the network response is in a loading state.
 *
 * This extension function allows you to handle the loading state of the network response.
 * It can be used to display a loading indicator or show any UI element that indicates
 * that the request is still in progress.
 *
 * @param action The action to be executed if the response is loading.
 * @return The current network response.
 */
inline fun <T> NetworkResponse<T>.onLoading(action: () -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Loading) {
        action()
    }
    return this
}

/**
 * Executes a given action if the network response is empty.
 *
 * This extension function handles cases where the network request returns no data,
 * often used when the response has no content but is still considered successful.
 *
 * @param action The action to be executed if the response is empty.
 * @return The current network response.
 */
inline fun <T> NetworkResponse<T>.onEmpty(action: () -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Empty) {
        action()
    }
    return this
}

/**
 * Executes a given action if the network response is an error.
 *
 * This extension function handles different types of errors (HTTP error, network error, etc.).
 * It allows you to define specific error handling logic based on the type of error encountered.
 *
 * @param action The action to be executed if the response is an error.
 * @return The current network response.
 */
inline fun <T> NetworkResponse<T>.onError(action: (NetworkResponse.Error) -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Error) {
        action(this)
    }
    return this
}