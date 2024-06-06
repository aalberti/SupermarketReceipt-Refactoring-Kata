package supermarket.model

abstract class Offer {
    abstract fun discount(
        catalog: SupermarketCatalog,
        productQuantity: ProductQuantity
    ): Discount?
}

abstract class ByProductOffer(val product: Product): Offer() {
    internal fun applies(product: Product) = product == this.product
}

class ThreeForTwoOffer(product: Product) : ByProductOffer(product) {
    override fun discount(
        catalog: SupermarketCatalog,
        productQuantity: ProductQuantity
    ): Discount? =
        if (applies(productQuantity.product) && productQuantity.quantity > 2) {
            val unitPrice = catalog.getUnitPrice(productQuantity.product)
            val numberOfDiscounts = productQuantity.quantity / 3
            val discountAmount =
                productQuantity.quantity * unitPrice - (numberOfDiscounts * 2.0 * unitPrice + productQuantity.quantity % 3 * unitPrice)
            Discount(productQuantity.product, "3 for 2", discountAmount)
        } else null
}

class TenPercentDiscountOffer(product: Product, private val percentDiscount: Double) : ByProductOffer(product) {
    override fun discount(
        catalog: SupermarketCatalog,
        productQuantity: ProductQuantity
    ): Discount? =
        if (applies(productQuantity.product)) Discount(
            productQuantity.product, "$percentDiscount% off",
            productQuantity.quantity * catalog.getUnitPrice(productQuantity.product) * percentDiscount / 100.0
        ) else null
}

class TwoForAmountOffer(product: Product, private val amount: Double) : ByProductOffer(product) {
    override fun discount(
        catalog: SupermarketCatalog,
        productQuantity: ProductQuantity
    ): Discount? =
        if (applies(productQuantity.product) && productQuantity.quantity >= 2) {
            val unitPrice = catalog.getUnitPrice(productQuantity.product)
            val total =
                amount * (productQuantity.quantity.toInt() / 2) + productQuantity.quantity % 2 * unitPrice
            val discountAmount = unitPrice * productQuantity.quantity - total
            Discount(productQuantity.product, "2 for $amount", discountAmount)
        } else null
}

class FiveForAmountOffer(product: Product, private val amount: Double) : ByProductOffer(product) {
    override fun discount(
        catalog: SupermarketCatalog,
        productQuantity: ProductQuantity
    ): Discount? =
        if (applies(productQuantity.product) && productQuantity.quantity >= 5) {
            val unitPrice = catalog.getUnitPrice(productQuantity.product)
            val numberOfDiscounts = productQuantity.quantity.toInt() / 5
            val discountTotal =
                unitPrice * productQuantity.quantity - (amount * numberOfDiscounts + productQuantity.quantity % 5 * unitPrice)
            Discount(productQuantity.product, "5 for $amount", discountTotal)
        } else null
}