package supermarket.model

class ShoppingCart {
    private val _items = ArrayList<ProductQuantity>()
    val items:List<ProductQuantity>
        get() = ArrayList(_items)

    fun addItem(product: Product, quantity: Double) {
        _items.add(ProductQuantity(product, quantity))
    }
}
