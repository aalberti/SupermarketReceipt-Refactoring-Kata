package supermarket.model

class ShoppingCart {
    private val items = ArrayList<ProductQuantity>()

    internal fun getItems(): List<ProductQuantity> {
        return ArrayList(items)
    }

    fun addItem(product: Product, quantity: Double) {
        items.add(ProductQuantity(product, quantity))
    }

    internal fun handleOffers(receipt: Receipt, offers: Map<Product, Offer>, catalog: SupermarketCatalog) {
        for (item in items) {
            item.quantity
            if (offers.containsKey(item.product)) {
                val offer = offers[item.product]!!
                offer.discount(catalog, item)
                    ?.let { receipt.addDiscount(it) }
            }
        }
    }
}
