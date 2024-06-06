package supermarket.model

import supermarket.model.SpecialOfferType.*

class Offer(private var offerType: SpecialOfferType, internal val product: Product, private var argument: Double) {
    fun discount(
        quantity: Double,
        catalog: SupermarketCatalog,
        product: Product
    ) = when (this.offerType) {
        TwoForAmount -> {
            val quantityAsInt = quantity.toInt()
            if (quantityAsInt >= 2) {
                val total =
                    this.argument * (quantityAsInt / 2) + quantityAsInt % 2 * catalog.getUnitPrice(product)
                val amount = catalog.getUnitPrice(product) * quantity - total
                Discount(product, "2 for " + this.argument, amount)
            } else null
        }
        ThreeForTwo -> {
            val quantityAsInt = quantity.toInt()
            if (quantityAsInt > 2) {
                val numberOfDiscounts = quantityAsInt / 3
                val discountAmount =
                    quantity * catalog.getUnitPrice(product) - (numberOfDiscounts.toDouble() * 2.0 * catalog.getUnitPrice(
                        product
                    ) + quantityAsInt % 3 * catalog.getUnitPrice(product))
                Discount(product, "3 for 2", discountAmount)
            } else null
        }
        TenPercentDiscount -> {
            Discount(
                product, this.argument.toString() + "% off",
                quantity * catalog.getUnitPrice(product) * this.argument / 100.0
            )
        }
        FiveForAmount -> {
            val quantityAsInt = quantity.toInt()
            if (quantityAsInt >= 5) {
                val numberOfDiscounts = quantityAsInt / 5
                val discountTotal =
                    catalog.getUnitPrice(product) * quantity - (this.argument * numberOfDiscounts + quantityAsInt % 5 * catalog.getUnitPrice(
                        product
                    ))
                Discount(product, 5.toString() + " for " + this.argument, discountTotal)
            } else null
        }
    }
}
