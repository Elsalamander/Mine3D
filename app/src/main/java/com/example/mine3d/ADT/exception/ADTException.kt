package com.example.mine3d.ADT.exception

open class ADTException : RuntimeException {
    /**
     *
     */
    constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super() {}

    /**
     * @param message
     * @param cause
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)

    /**
     * @param message
     */
    constructor(message: String?) : super(message) {}

    /**
     * @param cause
     */
    constructor(cause: Throwable?) : super(cause) {}

    companion object {
        /**
         *
         */
        private const val serialVersionUID = 4798924295945076164L
    }
}
