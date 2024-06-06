package supermarket.model

import supermarket.model.SpecialOfferType.*

class ShoppingCart {

    private val items = ArrayList<ProductQuantity>()
    internal var productQuantities: MutableMap<Product, Double> = HashMap()


    internal fun getItems(): List<ProductQuantity> {
        return ArrayList(items)
    }

    internal fun productQuantities(): Map<Product, Double> {
        return productQuantities
    }


    fun addItemQuantity(product: Product, quantity: Double) {
        items.add(ProductQuantity(product, quantity))
        if (productQuantities.containsKey(product)) {
            productQuantities[product] = productQuantities[product]!! + quantity
        } else {
            productQuantities[product] = quantity
        }
    }

    internal fun handleOffers(receipt: Receipt, offers: Map<Product, Offer>, catalog: SupermarketCatalog) {
        for (product in productQuantities().keys) {
            val quantity = productQuantities[product]!!
            if (offers.containsKey(product)) {
                val offer = offers[product]!!
                val discount: Discount? = when (offer.offerType) {
                    TwoForAmount -> {
                        val quantityAsInt = quantity.toInt()
                        if (quantityAsInt >= 2) {
                            val total =
                                offer.argument * (quantityAsInt / 2) + quantityAsInt % 2 * catalog.getUnitPrice(product)
                            val discountN = catalog.getUnitPrice(product) * quantity - total
                            Discount(product, "2 for " + offer.argument, discountN)
                        } else null
                    }
                    ThreeForTwo -> {
                        val quantityAsInt = quantity.toInt()
                        val numberOfXs = quantityAsInt / 3
                        if (quantityAsInt > 2) {
                            val discountAmount =
                                quantity * catalog.getUnitPrice(product) - (numberOfXs.toDouble() * 2.0 * catalog.getUnitPrice(
                                    product
                                ) + quantityAsInt % 3 * catalog.getUnitPrice(product))
                            Discount(product, "3 for 2", discountAmount)
                        } else null
                    }
                    TenPercentDiscount -> {
                        Discount(product, offer.argument.toString() + "% off",
                                quantity * catalog.getUnitPrice(product) * offer.argument / 100.0)
                    }
                    FiveForAmount -> {
                        val quantityAsInt = quantity.toInt()
                        val numberOfXs = quantityAsInt / 5
                        if (quantityAsInt >= 5) {
                            val discountTotal =
                                catalog.getUnitPrice(product) * quantity - (offer.argument * numberOfXs + quantityAsInt % 5 * catalog.getUnitPrice(
                                    product
                                ))
                            Discount(product, 5.toString() + " for " + offer.argument, discountTotal)
                        } else null
                    }
                }
                if (discount != null)
                    receipt.addDiscount(discount)
            }
        }
    }
}
