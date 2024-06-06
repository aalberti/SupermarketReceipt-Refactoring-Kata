package supermarket.model

enum class SpecialOfferType {
    ThreeForTwo, TenPercentDiscount, TwoForAmount, FiveForAmount;

    fun applies(productQuantity: ProductQuantity): Boolean = true
}
