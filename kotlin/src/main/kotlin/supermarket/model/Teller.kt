package supermarket.model

import java.util.HashMap

class Teller(private val catalog: SupermarketCatalog) {
    private val offers = HashMap<Product, Offer>()

    fun addSpecialOffer(offer: Offer) {
        this.offers[offer.product] = offer
    }

    fun checksOutArticlesFrom(cart: ShoppingCart): Receipt {
        val receipt = Receipt()
        val productQuantities = cart.items
        for (pq in productQuantities) {
            val p = pq.product
            val quantity = pq.quantity
            val unitPrice = this.catalog.getUnitPrice(p)
            val price = quantity * unitPrice
            receipt.addProduct(p, quantity, unitPrice, price)
        }
        for (item in cart.items) {
            offers[item.product]?.let { offer ->
                offer.discount(catalog, item)?.let { discount -> receipt.addDiscount(discount) }
            }
        }

        return receipt
    }
}
