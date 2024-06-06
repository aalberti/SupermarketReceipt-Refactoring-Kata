package supermarket.model

class Teller(private val catalog: SupermarketCatalog) {
    private val offers = ArrayList<Offer>()

    fun addSpecialOffer(offer: Offer) {
        if (offer is ByProductOffer)
            offers.firstOrNull { it is ByProductOffer && it.product == offer.product }
                ?.let { offers.remove(it) }
        offers.add(offer)
    }

    fun checksOutArticlesFrom(cart: ShoppingCart): Receipt {
        val receipt = Receipt()
        val productQuantities = cart.items
        for (pq in productQuantities) {
            val p = pq.product
            val quantity = pq.quantity
            val unitPrice = catalog.getUnitPrice(p)
            val price = quantity * unitPrice
            receipt.addProduct(p, quantity, unitPrice, price)
        }
//==== NEXT: offers work on the cart, not cart items. Offers are thus not per product. There can thus be multiple offers per product (check tests)
        for (item in cart.items)
            for (offer in offers)
                offer.discount(catalog, item)?.let { discount -> receipt.addDiscount(discount) }

        return receipt
    }
}
