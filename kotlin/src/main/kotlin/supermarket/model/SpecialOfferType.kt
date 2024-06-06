package supermarket.model

enum class SpecialOfferType {
    ThreeForTwo {
        override fun applies(productQuantity: ProductQuantity): Boolean =
            productQuantity.quantity.toInt() > 2
    }, TenPercentDiscount {
        override fun applies(productQuantity: ProductQuantity): Boolean = true
    }, TwoForAmount {
        override fun applies(productQuantity: ProductQuantity): Boolean =
            productQuantity.quantity.toInt() >= 2
    }, FiveForAmount {
        override fun applies(productQuantity: ProductQuantity): Boolean =
            productQuantity.quantity.toInt() >= 5
    };

    abstract fun applies(productQuantity: ProductQuantity): Boolean
}
