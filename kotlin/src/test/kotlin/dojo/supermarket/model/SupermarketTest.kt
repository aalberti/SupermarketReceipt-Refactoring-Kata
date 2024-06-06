package dojo.supermarket.model

import org.approvaltests.Approvals.verify
import org.junit.jupiter.api.Test
import supermarket.ReceiptPrinter
import supermarket.model.*

class SupermarketTest {
    private val toothbrush = Product("toothbrush", ProductUnit.Each)
    private val apples = Product("apples", ProductUnit.Kilo)
    private val teller: Teller by lazy {
        val catalog = FakeCatalog()
        catalog.addProduct(toothbrush, 0.99)
        catalog.addProduct(apples, 1.99)
        Teller(catalog)
    }
    private val printer = ReceiptPrinter()

    @Test
    fun `no discount`() {
        val cart = ShoppingCart()
        cart.addItem(apples, 2.5)

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `add twice`() {
        val cart = ShoppingCart()
        cart.addItem(apples, 1.0)
        cart.addItem(apples, 1.0)

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `10 percent discount`() {
        val cart = ShoppingCart()
        cart.addItem(toothbrush, 1.0)
        teller.addSpecialOffer(TenPercentDiscountOffer(toothbrush, 10.0))

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `discount for another product doesn't apply`() {
        val cart = ShoppingCart()
        cart.addItem(apples, 1.0)
        teller.addSpecialOffer(TenPercentDiscountOffer(toothbrush, 10.0))

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `3 for 2`() {
        val cart = ShoppingCart()
        cart.addItem(toothbrush, 3.0)
        teller.addSpecialOffer(ThreeForTwoOffer(toothbrush))

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `3 for 2 doesn't apply`() {
        val cart = ShoppingCart()
        cart.addItem(toothbrush, 2.0)
        teller.addSpecialOffer(ThreeForTwoOffer(toothbrush))

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `2 for amount`() {
        val cart = ShoppingCart()
        cart.addItem(toothbrush, 3.0)
        teller.addSpecialOffer(TwoForAmountOffer(toothbrush, 1.50))

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `2 for amount twice`() {
        val cart = ShoppingCart()
        cart.addItem(toothbrush, 4.0)
        teller.addSpecialOffer(TwoForAmountOffer(toothbrush, 1.50))

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `2 for amount doesn't apply`() {
        val cart = ShoppingCart()
        cart.addItem(toothbrush, 1.0)
        teller.addSpecialOffer(TwoForAmountOffer(toothbrush, 1.50))

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `5 for amount`() {
        val cart = ShoppingCart()
        cart.addItem(toothbrush, 6.0)
        teller.addSpecialOffer(FiveForAmountOffer(toothbrush, 2.99))

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `5 for amount twice`() {
        val cart = ShoppingCart()
        cart.addItem(toothbrush, 12.0)
        teller.addSpecialOffer(FiveForAmountOffer(toothbrush, 5.99))

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `5 for amount doesn't apply`() {
        val cart = ShoppingCart()
        cart.addItem(toothbrush, 4.0)
        teller.addSpecialOffer(FiveForAmountOffer(toothbrush, 2.99))

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }
}
