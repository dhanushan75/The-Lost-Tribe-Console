package com.tlt.console.views.bookings;

import com.tlt.console.service.BookingService;
import com.tlt.console.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

//@PageTitle("Bookings")
//@Route(value = "bookings", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
public class BookingView extends VerticalLayout {

    private HorizontalLayout mContent;

    private Tab mSummary;

    private Tab mMakeABooking;

    private SummaryView mSummaryView;

    private MakeABookingView mMakeABookingView;

    public BookingView(@Autowired BookingService mBookingService) {

        this.setSizeFull();

        mSummary = new Tab("Summary");
        mMakeABooking = new Tab("Make A Booking");

        Tabs tabs = new Tabs(mSummary, mMakeABooking);
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.setWidthFull();

        mContent = new HorizontalLayout();
        mContent.setSpacing(false);
        mContent.setSizeFull();

        add(tabs);
        add(mContent);

        tabs.addSelectedChangeListener(event ->
                setContent(event.getSelectedTab(), mBookingService)
        );

        setContent(tabs.getSelectedTab(), mBookingService);

    }

    private void setContent(Tab tab, BookingService pBookingService) {
        mContent.removeAll();

        if (tab.equals(mSummary)) {
            mSummaryView = new SummaryView(pBookingService);
            mContent.add(mSummaryView);
        } else if (tab.equals(mMakeABooking)) {
            mMakeABookingView = new MakeABookingView(pBookingService);
            mContent.add(mMakeABookingView);
        }
    }

}
