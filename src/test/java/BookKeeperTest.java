import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    private BookKeeper bookKeeper;
    private Invoice invoice;
    private InvoiceFactory invoiceFactory;
    private InvoiceRequest invoiceRequest;
    private InvoiceLine invoiceLine;
    private Money money;
    private Tax tax;
    private ClientData clientData;

    @Before
    public void setUp(){
        clientData = new ClientData(Id.generate(), "Testclient");
        invoiceFactory = new InvoiceFactory();
        money = new Money(100);
        tax = new Tax(money, "podatek od wdechu");
    }

}
