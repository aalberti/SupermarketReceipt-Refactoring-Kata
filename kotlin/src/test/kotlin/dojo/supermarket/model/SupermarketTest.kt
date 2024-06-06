package dojo.supermarket.model

import org.assertj.core.api.IterableAssert
import org.assertj.core.api.KotlinAssertions.assertThat
import org.assertj.core.util.DoubleComparator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import supermarket.model.*
import supermarket.model.SpecialOfferType.*

class SupermarketTest {
    private lateinit var catalog: FakeCatalog
    private lateinit var teller: Teller
    private val toothbrush = Product("toothbrush", ProductUnit.Each)
    private val apples = Product("apples", ProductUnit.Kilo)

    @BeforeEach
    fun setup() {
        catalog = FakeCatalog()
        catalog.addProduct(toothbrush, 0.99)
        catalog.addProduct(apples, 1.99)
        teller = Teller(catalog)
    }

    @Test
    fun `no discount`() {
        val cart = ShoppingCart()
        cart.addItemQuantity(apples, 2.5)

        val receipt = teller.checksOutArticlesFrom(cart)

        receipt.assertIs(
            listOf(ReceiptItem(apples, 2.5, 1.99, 4.975)),
            listOf(),
            4.975
        )
    }

    @Test
    fun `10 percent discount`() {
        val cart = ShoppingCart()
        cart.addItemQuantity(toothbrush, 1.0)

        teller.addSpecialOffer(TenPercentDiscount, toothbrush, 10.0)

        val receipt = teller.checksOutArticlesFrom(cart)

        receipt.assertIs(
            listOf(ReceiptItem(toothbrush, 1.0, 0.99, 0.99)),
            listOf(Discount(toothbrush, "10.0% off", 0.099)),
            0.99 - 0.099
        )
    }

    @Test
    fun `3 for 2`() {
        val cart = ShoppingCart()
        cart.addItemQuantity(toothbrush, 3.0)

        teller.addSpecialOffer(ThreeForTwo, toothbrush, 10.0)

        val receipt = teller.checksOutArticlesFrom(cart)

        receipt.assertIs(
            listOf(ReceiptItem(toothbrush, 3.0, 0.99, 2.97)),
            listOf(Discount(toothbrush, "3 for 2", 0.99)),
            0.99 * 2
        )
    }

    @Test
    fun `2 for amount`() {
        val cart = ShoppingCart()
        cart.addItemQuantity(toothbrush, 3.0)

        teller.addSpecialOffer(TwoForAmount, toothbrush, 1.5)

        val receipt = teller.checksOutArticlesFrom(cart)

        receipt.assertIs(
            listOf(ReceiptItem(toothbrush, 3.0, 0.99, 2.97)),
            listOf(Discount(toothbrush, "2 for 1.5", 0.48)),
            2.49
        )
    }

    @Test
    fun `2 for amount twice`() {
        val cart = ShoppingCart()
        cart.addItemQuantity(toothbrush, 4.0)

        teller.addSpecialOffer(TwoForAmount, toothbrush, 1.5)

        val receipt = teller.checksOutArticlesFrom(cart)

        receipt.assertIs(
            listOf(ReceiptItem(toothbrush, 4.0, 0.99, 3.96)),
            listOf(Discount(toothbrush, "2 for 1.5", 0.96)),
            3.0
        )
    }

    private fun Receipt.assertIs(
        receiptItems: List<ReceiptItem>, discounts: List<Discount>, totalPrice: Double
    ) = assertAll({
        assertThatList(this.getItems())
            .isEqualTo(
                receiptItems
            )
    }, {
        assertThatList(this.getDiscounts())
            .isEqualTo(
                discounts
            )
    }, {
        assertThat(this.totalPrice)
            .usingComparator(DoubleComparator(0.001))
            .isEqualTo(totalPrice)
    })

    private fun <T> assertThatList(list: List<T>): IterableAssert<T> =
        assertThat(list)
            .usingFieldByFieldElementComparator()
            .usingComparatorForElementFieldsWithNames(
                DoubleComparator(0.001),
                "discountAmount", "totalPrice"
            )
}
