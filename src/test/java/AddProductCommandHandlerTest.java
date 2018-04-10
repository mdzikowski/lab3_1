import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddProductCommandHandlerTest {

    private AddProductCommandHandler handler;
    private ReservationRepository reservationRepo;
    private ClientRepository clientRepo;
    private ProductRepository productRepo;
    private SuggestionService suggestionServ;
    private AddProductCommand addProductCommand;
    private Product product;
    private Product suggestedProduct;
    private Product productNotAvailable;
    private Reservation reservation;
    private Client client;

    @Before
    public void setup(){
        handler = new AddProductCommandHandler();
        reservationRepo = mock(ReservationRepository.class);
        clientRepo = mock(ClientRepository.class);
        productRepo = mock(ProductRepository.class);
        suggestionServ = mock(SuggestionService.class);
        product = new ProductBuilder().withId(Id.generate()).withName("sampleProduct").withPrice(new Money(100)).withType(ProductType.DRUG).build();

        client = new Client();
        reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, new ClientData(Id.generate(), "Kowalski"), new Date());
        addProductCommand = new AddProductCommand(Id.generate(), product.getId(), 11);


        Whitebox.setInternalState(handler, "reservationRepository", reservationRepo);
        Whitebox.setInternalState(handler, "clientRepository", clientRepo);
        Whitebox.setInternalState(handler, "productRepository", productRepo);
        Whitebox.setInternalState(handler, "suggestionService", suggestionServ);

        when(reservationRepo.load(addProductCommand.getOrderId())).thenReturn(reservation);

    }
    @Test
    public void addProductCommandShouldNotSuggestEquivalentWhenGivenAvailableProduct(){
        when(productRepo.load(addProductCommand.getProductId())).thenReturn(product);
        handler.handle(addProductCommand);

        verify(suggestionServ, Mockito.times(0)).suggestEquivalent(product, client);
    }
    @Test
    public void saveMethodShouldBeCalledOnceWhenGivenAvailableItem(){
        when(productRepo.load(addProductCommand.getProductId())).thenReturn(product);
        handler.handle(addProductCommand);

        verify(reservationRepo, Mockito.times(1)).save(reservation);
    }
}
