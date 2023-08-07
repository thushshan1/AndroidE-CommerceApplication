package com.example.b07group19;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    MainActivity view;

    @Mock
    Model model;

    @Captor
    private ArgumentCaptor<Consumer<String>> captor;

    @Test
    public void testShopperLoginSus() {
        // Test log in as customer

        String email = "a@mail.com";
        String password = "123456";
        String type = "user";

        Presenter presenter = new Presenter(model, view);

        presenter.login(email, password);

        // verify if model.authenticate() has run
        verify(model).authenticate(eq(email), eq(password), captor.capture());
        Consumer<String> callback = captor.getValue();
        callback.accept(type);

        // verify if view.redirectToPatientDashboard has run
        verify(view, times(1)).redirectToCustomerDashboard();
    }

    @Test
    public void testShopperLoginFailure() {
        // Test log in as customer

        String email = "a@mail.com";
        String password = "123456";
        String type = null;

        Presenter presenter = new Presenter(model, view);

        presenter.login(email, password);

        // verify if model.authenticate() has run
        verify(model).authenticate(eq(email), eq(password), captor.capture());
        Consumer<String> callback = captor.getValue();
        callback.accept(type);

        // verify if view.redirectToPatientDashboard has run
        verify(view, times(1)).failedToLogin();
    }

    @Test
    public void testOwnerLoginSus() {
        // Test log in as customer

        String email = "a@mail.com";
        String password = "123456";
        String type = "owner";

        Presenter presenter = new Presenter(model, view);

        presenter.login(email, password);

        // verify if model.authenticate() has run
        verify(model).authenticate(eq(email), eq(password), captor.capture());
        Consumer<String> callback = captor.getValue();
        callback.accept(type);

        // verify if view.redirectToPatientDashboard has run
        verify(view, times(1)).redirectToStoreDashboard();
    }

    @Test
    public void testOwnerLoginFailure() {
        // Test log in as customer

        String email = "a@mail.com";
        String password = "123456";
        String type = null;

        Presenter presenter = new Presenter(model, view);

        presenter.login(email, password);

        // verify if model.authenticate() has run
        verify(model).authenticate(eq(email), eq(password), captor.capture());
        Consumer<String> callback = captor.getValue();
        callback.accept(type);

        // verify if view.redirectToPatientDashboard has run
        verify(view, times(1)).failedToLogin();
    }
}