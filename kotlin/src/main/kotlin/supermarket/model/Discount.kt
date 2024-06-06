package supermarket.model

open class Discount(open val description: String, open val discountAmount: Double)
data class ProductDiscount(
    val product: Product,
    override val description: String,
    override val discountAmount: Double
) :
    Discount(description, discountAmount)
