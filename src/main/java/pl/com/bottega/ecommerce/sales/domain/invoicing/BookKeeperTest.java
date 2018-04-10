package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookKeeperTest {

    private BookKeeper bookKeeper;
    private TaxPolicy mockedTaxPolicy = mock(TaxPolicy.class);
    private Id id = new Id("123");


    @Test public void invoiceWithOnePosition() throws Exception {

        TaxPolicy mockedTaxPolicy = mock(TaxPolicy.class);
        Id id = new Id("123");
        ClientData client = new ClientData(id, "test");
        InvoiceRequest invoiceRequest = new InvoiceRequest(client);

        InvoiceFactory mockedInvoiceFactory = mock(InvoiceFactory.class);
        bookKeeper = new BookKeeper(mockedInvoiceFactory);

        ProductData product = new ProductData(id,Money.ZERO,"klawiatura",ProductType.STANDARD,new Date());
        RequestItem item = new RequestItem(product,1,Money.ZERO);
        invoiceRequest.add(item);

        when(mockedInvoiceFactory.create(client)).thenReturn(new Invoice(id,client));
        when(mockedTaxPolicy.calculateTax(ProductType.STANDARD,Money.ZERO)).thenReturn(new Tax(Money.ZERO,"tax"));

        Invoice invoiceResult = bookKeeper.issuance(invoiceRequest,mockedTaxPolicy);

        assertThat(invoiceResult.getItems().size(),is(1));

    }
    @Test public void invoiceWithTwoPositionsShouldCallCalculateTaxMethodTwice() throws Exception{

        ClientData client = new ClientData(id, "test");
        InvoiceRequest invoiceRequest = new InvoiceRequest(client);

        InvoiceFactory mockedInvoiceFactory = mock(InvoiceFactory.class);
        bookKeeper = new BookKeeper(mockedInvoiceFactory);

        ProductData product = new ProductData(id,Money.ZERO,"klawiatura",ProductType.STANDARD,new Date());
        RequestItem item = new RequestItem(product,1,Money.ZERO);
        invoiceRequest.add(item);
        invoiceRequest.add(item);

        when(mockedInvoiceFactory.create(client)).thenReturn(new Invoice(id,client));
        when(mockedTaxPolicy.calculateTax(ProductType.STANDARD,Money.ZERO)).thenReturn(new Tax(Money.ZERO,"tax"));

        Invoice invoiceResult = bookKeeper.issuance(invoiceRequest,mockedTaxPolicy);

        verify(mockedTaxPolicy,times(2)).calculateTax(ProductType.STANDARD,Money.ZERO);
}
    @Test public void invoiceWithoutPositionsShouldReturnEmptyInvoice() throws Exception{

        ClientData client = new ClientData(id, "test");
        InvoiceRequest invoiceRequest = new InvoiceRequest(client);

        InvoiceFactory mockedInvoiceFactory = mock(InvoiceFactory.class);
        bookKeeper = new BookKeeper(mockedInvoiceFactory);


        when(mockedInvoiceFactory.create(client)).thenReturn(new Invoice(id,client));
        when(mockedTaxPolicy.calculateTax(ProductType.STANDARD,Money.ZERO)).thenReturn(new Tax(Money.ZERO,"tax"));

        Invoice invoiceResult = bookKeeper.issuance(invoiceRequest,mockedTaxPolicy);

        assertThat(invoiceResult.getItems().size(),is(0));

    }
}