import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    private BookKeeper bookKeeper;
    private Invoice invoice;
    private InvoiceFactory invoiceFactory;
    private InvoiceRequest invoiceRequest;
    private InvoiceLine invoiceLine;
    private TaxPolicy taxPolicy;
    private ClientData clientData;

    @Before
    public void setUp(){
        clientData = new ClientData(Id.generate(), "Testclient");
        invoiceFactory = new InvoiceFactory();
        taxPolicy = mock(TaxPolicy.class);
        invoiceRequest = mock(InvoiceRequest.class);
        bookKeeper = new BookKeeper( new InvoiceFactory());
    }

    @Test
    public void invoiceRequestWithOneItemShouldReturnInvoiceWithOneItem(){
        ProductData productData = new ProductData(Id.generate(), new Money(10), "Item", ProductType.FOOD, new Date());
        RequestItem requestItem = new RequestItem(productData, 10, new Money(10));

        when(invoiceRequest.getItems()).thenReturn(Collections.singletonList(requestItem));
        when(taxPolicy.calculateTax(ProductType.FOOD, requestItem.getTotalCost())) .thenReturn(new Tax(new Money(0.25), "Item Tax"));

        Invoice resultInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(resultInvoice.getItems().size(), is(1));
    }

    @Test
    public void invoiceRequestShouldCallCalculateTaxTwiceWhenGivenTwoItems(){
        ProductData productData1 = new ProductData(Id.generate(), new Money(10), "Item", ProductType.FOOD, new Date());
        ProductData productData2 = new ProductData(Id.generate(), new Money(10), "Item", ProductType.FOOD, new Date());
        RequestItem requestItem1 = new RequestItem(productData1, 1, new Money(10));
        RequestItem requestItem2 = new RequestItem(productData2, 2, new Money(20));
        Tax tax = new Tax(new Money(0.25), "Item Tax");

        when(invoiceRequest.getItems()).thenReturn(Arrays.asList(requestItem1, requestItem2));
        when(taxPolicy.calculateTax(requestItem1.getProductData().getType(), requestItem1.getTotalCost())).thenReturn(tax);
        when(taxPolicy.calculateTax(requestItem2.getProductData().getType(), requestItem2.getTotalCost())).thenReturn(tax);

        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(1)).calculateTax(requestItem1.getProductData().getType(), requestItem1.getTotalCost());
        verify(taxPolicy, times(1)).calculateTax(requestItem2.getProductData().getType(), requestItem2.getTotalCost());
    }

    @Test
    public void invoiceRequestWithNoItemsShouldReturnInvoiceWithNoItems(){
        ProductData productData = new ProductData(Id.generate(), new Money(10), "Item", ProductType.FOOD, new Date());
        RequestItem requestItem = new RequestItem(productData, 10, new Money(10));

        when(invoiceRequest.getItems()).thenReturn(Collections.<RequestItem>emptyList());
        when(taxPolicy.calculateTax(ProductType.FOOD, requestItem.getTotalCost())) .thenReturn(new Tax(new Money(0.25), "Item Tax"));

        Invoice resultInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(resultInvoice.getItems().size(), is(0));
    }

    @Test
    public void invoiceRequestShouldReturnCorrectGrosValueWhenHavingTwoItems(){
        ProductData productData1 = new ProductData(Id.generate(), new Money(20), "Item", ProductType.FOOD, new Date());
        ProductData productData2 = new ProductData(Id.generate(), new Money(10), "Item", ProductType.FOOD, new Date());
        RequestItem requestItem1 = new RequestItem(productData1, 1, new Money(10));
        RequestItem requestItem2 = new RequestItem(productData2, 2, new Money(20));
        Tax tax = new Tax(new Money(0.25), "Item Tax");

        when(invoiceRequest.getItems()).thenReturn(Arrays.asList(requestItem1, requestItem2));
        when(taxPolicy.calculateTax(requestItem1.getProductData().getType(), requestItem1.getTotalCost())).thenReturn(tax);
        when(taxPolicy.calculateTax(requestItem2.getProductData().getType(), requestItem2.getTotalCost())).thenReturn(tax);

        Invoice resultInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(resultInvoice.getGros(), is(new Money(30.5)));
    }

}
