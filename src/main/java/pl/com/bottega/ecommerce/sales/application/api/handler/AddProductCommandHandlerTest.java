package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AddProductCommandHandlerTest {
    AddProductCommandHandler commandHandler;
    AddProductCommand productCommand;
    Id id = new Id("123");

    @Mock
    Reservation mockedReservationRepository;
    AddProductCommandHandler addProductCommandHandler;
    @Test public void methodSaveIsCalled() throws Exception {

        commandHandler = new AddProductCommandHandler();
        productCommand= new AddProductCommand(id,id,5);

        ReservationRepository mockedReservation = mock(ReservationRepository.class);
        ProductRepository mockedProduct = mock(ProductRepository.class);

        Reservation reservation = new Reservation();
        Product product = new Product();

        //Dzialalo by na starszej wersji mockito :c
        Whitebox.setInternalState(commandHandler,"reservationRepository",reservation);

        when(mockedReservation.load(Mockito.any(Id.class))).thenReturn(reservation);
        when(mockedProduct.load(Mockito.any(Id.class))).thenReturn(product);

        commandHandler.handle(productCommand);


        verify(mockedReservation,times(1)).save(reservation);
        //verify(commandHandler,times(1)).handle(productCommand);
        //commandHandler.handle(productCommand);


    }

}