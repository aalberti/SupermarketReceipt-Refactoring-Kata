package supermarket.model

abstract class Offer(val product: Product) {
    fun discount(
        catalog: SupermarketCatalog,
        productQuantity: ProductQuantity
    ): Discount? =
        if (applies(productQuantity))
            discount(productQuantity, catalog.getUnitPrice(productQuantity.product))
        else null

    abstract fun discount(
        productQuantity: ProductQuantity,
        unitPrice: Double
    ):  Discount

    abstract fun applies(productQuantity: ProductQuantity): Boolean
}

class ThreeForTwoOffer(product: Product) :
    Offer(product) {
    override fun discount(productQuantity: ProductQuantity, unitPrice: Double): Discount {
        val numberOfDiscounts = productQuantity.quantity.toInt() / 3
        val discountAmount =
            productQuantity.quantity * unitPrice - (numberOfDiscounts.toDouble() * 2.0 * unitPrice + productQuantity.quantity % 3 * unitPrice)
        return Discount(productQuantity.product, "3 for 2", discountAmount)
    }

    override fun applies(productQuantity: ProductQuantity): Boolean {
        return productQuantity.quantity.toInt() > 2
    }
}

class TenPercentDiscountOffer(product: Product, private val percentDiscount: Double) :
    Offer(product) {
    override fun applies(productQuantity: ProductQuantity): Boolean = true

    override fun discount(
        productQuantity: ProductQuantity,
        unitPrice: Double
    ): Discount = Discount(
        productQuantity.product, "$percentDiscount% off",
        productQuantity.quantity * unitPrice * percentDiscount / 100.0
    )
}

class TwoForAmountOffer(product: Product, private val amount: Double) :
    Offer(product) {
    override fun applies(productQuantity: ProductQuantity): Boolean =
        productQuantity.quantity.toInt() >= 2

    override fun discount(
        productQuantity: ProductQuantity,
        unitPrice: Double
    ): Discount {
        val total =
            amount * (productQuantity.quantity.toInt() / 2) + productQuantity.quantity % 2 * unitPrice
        val discountAmount = unitPrice * productQuantity.quantity - total
        return Discount(productQuantity.product, "2 for ${this.amount}", discountAmount)
    }
}

class FiveForAmountOffer(product: Product, private val amount: Double) :
    Offer(product) {
    override fun applies(productQuantity: ProductQuantity): Boolean =
        productQuantity.quantity.toInt() >= 5

    override fun discount(
        productQuantity: ProductQuantity,
        unitPrice: Double
    ): Discount {
        val numberOfDiscounts = productQuantity.quantity.toInt() / 5
        val discountTotal =
            unitPrice * productQuantity.quantity - (amount * numberOfDiscounts + productQuantity.quantity % 5 * unitPrice)
        return Discount(productQuantity.product, "5 for $amount", discountTotal)
    }
}