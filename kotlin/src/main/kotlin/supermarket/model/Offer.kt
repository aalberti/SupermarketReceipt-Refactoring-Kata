package supermarket.model

abstract class Offer {
    abstract fun discount(
        cart: ShoppingCart,
        catalog: SupermarketCatalog
    ): Discount?
}

abstract class ByProductOffer(val product: Product): Offer() {
    override fun discount(
        cart: ShoppingCart,
        catalog: SupermarketCatalog
    ): Discount? = cart.items
        .filter { this.applies(it) }
        .map { this.discount(catalog, it) }
        .firstOrNull()

    internal open fun applies(productQuantity: ProductQuantity) =
        productQuantity.product == this.product

    abstract fun discount(catalog: SupermarketCatalog, productQuantity: ProductQuantity): Discount
}

class ThreeForTwoOffer(product: Product) : ByProductOffer(product) {
    override fun discount(
        catalog: SupermarketCatalog,
        productQuantity: ProductQuantity
    ): Discount {
        val unitPrice = catalog.getUnitPrice(productQuantity.product)
        val numberOfDiscounts = productQuantity.quantity / 3
        val discountAmount =
            productQuantity.quantity * unitPrice - (numberOfDiscounts * 2.0 * unitPrice + productQuantity.quantity % 3 * unitPrice)
        return Discount(productQuantity.product, "3 for 2", discountAmount)
    }

    override fun applies(productQuantity: ProductQuantity) =
        super.applies(productQuantity) && productQuantity.quantity > 2
}

class TenPercentDiscountOffer(product: Product, private val percentDiscount: Double) : ByProductOffer(product) {
    override fun discount(
        catalog: SupermarketCatalog,
        productQuantity: ProductQuantity
    ) = Discount(
        productQuantity.product, "$percentDiscount% off",
        productQuantity.quantity * catalog.getUnitPrice(productQuantity.product) * percentDiscount / 100.0
    )
}

class TwoForAmountOffer(product: Product, private val amount: Double) : ByProductOffer(product) {
    override fun discount(
        catalog: SupermarketCatalog,
        productQuantity: ProductQuantity
    ): Discount {
        val unitPrice = catalog.getUnitPrice(productQuantity.product)
        val total =
            amount * (productQuantity.quantity.toInt() / 2) + productQuantity.quantity % 2 * unitPrice
        val discountAmount = unitPrice * productQuantity.quantity - total
        return Discount(productQuantity.product, "2 for $amount", discountAmount)
    }

    override fun applies(productQuantity: ProductQuantity) =
        super.applies(productQuantity) && productQuantity.quantity >= 2
}

class FiveForAmountOffer(product: Product, private val amount: Double) : ByProductOffer(product) {
    override fun discount(
        catalog: SupermarketCatalog,
        productQuantity: ProductQuantity
    ): Discount {
        val unitPrice = catalog.getUnitPrice(productQuantity.product)
        val numberOfDiscounts = productQuantity.quantity.toInt() / 5
        val discountTotal =
            unitPrice * productQuantity.quantity - (amount * numberOfDiscounts + productQuantity.quantity % 5 * unitPrice)
        return Discount(productQuantity.product, "5 for $amount", discountTotal)
    }

    override fun applies(productQuantity: ProductQuantity) =
        super.applies(productQuantity) && productQuantity.quantity >= 5
}