package dojo.supermarket.model

import org.approvaltests.Approvals.verify
import org.junit.jupiter.api.Test
import supermarket.ReceiptPrinter
import supermarket.model.Product
import supermarket.model.ProductUnit
import supermarket.model.ShoppingCart
import supermarket.model.SpecialOfferType.*
import supermarket.model.Teller

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
        cart.addItemQuantity(apples, 2.5)

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `10 percent discount`() {
        val cart = ShoppingCart()
        cart.addItemQuantity(toothbrush, 1.0)
        teller.addSpecialOffer(TenPercentDiscount, toothbrush, 10.0)

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `3 for 2`() {
        val cart = ShoppingCart()
        cart.addItemQuantity(toothbrush, 3.0)
        teller.addSpecialOffer(ThreeForTwo, toothbrush, 10.0)

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `2 for amount`() {
        val cart = ShoppingCart()
        cart.addItemQuantity(toothbrush, 3.0)
        teller.addSpecialOffer(TwoForAmount, toothbrush, 1.5)

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }

    @Test
    fun `2 for amount twice`() {
        val cart = ShoppingCart()
        cart.addItemQuantity(toothbrush, 4.0)
        teller.addSpecialOffer(TwoForAmount, toothbrush, 1.5)

        val receipt = teller.checksOutArticlesFrom(cart)

        verify(printer.printReceipt(receipt))
    }
}
