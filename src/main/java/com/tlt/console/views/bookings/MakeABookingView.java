package com.tlt.console.views.bookings;

import com.tlt.console.entity.ClientCheckInCalendarEntity;
import com.tlt.console.entity.UnitsEntity;
import com.tlt.console.service.BookingService;
import com.tlt.console.views.MainLayout;
import com.tlt.console.views.MyNotification;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@PageTitle("Bookings")
@Route(value = "Bookings", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
public class MakeABookingView extends VerticalLayout {

    private ComboBox<UnitsEntity> mMainUnitCombo;

    private ComboBox<UnitsEntity> mSubUnitCombo;

    private BookingService mBookingService;

    private FullCalendar calendar;

    private List<UnitsEntity> parentUnitList;

    private HashMap<Long, String> parentIdNameMap;

    private List<String> pColourList;

    private Button buttonDatePicker;

    private MenuBar mMenuBar;

    private CalendarView selectedView;

    public MakeABookingView(@Autowired BookingService pBookingService) {
        mBookingService = pBookingService;
        createControls();
    }

    private void createControls() {

        try {

            this.setSizeFull();

            selectedView = CalendarViewImpl.DAY_GRID_MONTH;

            pColourList = new ArrayList<>();
            pColourList.add("#ff3333");
            pColourList.add("#25383C");
            pColourList.add("#ADD8E6");
            pColourList.add("#800080");
            pColourList.add("#FFA500");
            pColourList.add("#646D7E");
            pColourList.add("#A52A2A");
            pColourList.add("#808080");
            pColourList.add("#7FFD4");

            ComboBox.ItemFilter<UnitsEntity> filter = (unitsEntity, filterString) -> (unitsEntity.getName().toLowerCase().startsWith(filterString.toLowerCase()));

            mSubUnitCombo = new ComboBox<>("Room / Dorm");
            mSubUnitCombo.setAllowCustomValue(false);

            mMainUnitCombo = new ComboBox<>("Unit");
            mMainUnitCombo.setAllowCustomValue(false);
            parentUnitList = mBookingService.getParentUnits();
            mMainUnitCombo.setItems(filter, parentUnitList);
            mMainUnitCombo.setItemLabelGenerator(units -> units.getName());

            parentIdNameMap = new HashMap<>();
            parentUnitList.stream().forEach(unit -> parentIdNameMap.put(unit.getUnitId(), unit.getName()));

            mMainUnitCombo.addValueChangeListener(value -> {
                setValuesToSubUnitCombobox(value.getValue());
            });

            Button mSearchButton = new Button(new Icon(VaadinIcon.CHECK));
            mSearchButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            mSearchButton.getElement().setAttribute("aria-label", "Check");
            mSearchButton.addClickListener(clickEvent -> {
                getCalendarEvents();
            });

            Button mClearButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
            mClearButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            mClearButton.getElement().setAttribute("aria-label", "Clear");
            mClearButton.addClickListener(clickEvent -> {
                mSubUnitCombo.clear();
                mMainUnitCombo.clear();
            });

            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.add(mSearchButton, mClearButton);

            FormLayout formLayout = new FormLayout();
            formLayout.add(mMainUnitCombo, mSubUnitCombo, buttonLayout);
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3));
            formLayout.setWidthFull();

            // Create a new calendar instance and attach it to our layout
            calendar = FullCalendarBuilder.create()
                    .withAutoBrowserTimezone()
//                    .withEntryLimit(3)
                    .build();
            calendar.setSizeFull();
            calendar.setLocale(Locale.ENGLISH);
            //        setFlexGrow(1, calendar);
//        calendar.setHeight("40%");
//        calendar.setWidth("40%");

            calendar.addTimeslotClickedListener(event -> {
                if (mMainUnitCombo.getValue() != null && mSubUnitCombo.getValue() != null && event != null) {
                    new MyNotification().error("Room already Booked for the day!!");
                } else {

                }
            });

            Button mAddCustomer = new Button("Add new Customer");
            mAddCustomer.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            mAddCustomer.setEnabled(true);

            mAddCustomer.addClickListener(clickEvent -> {
                AddNewCustomerDialog addNewCustomerDialog = new AddNewCustomerDialog(mBookingService, this);
            });

            mMenuBar = new MenuBar();
            mMenuBar.setWidthFull();

            FormLayout toolBar = new FormLayout();
            toolBar.add(new HorizontalLayout(), mMenuBar, new HorizontalLayout());
            toolBar.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3));
            toolBar.setWidthFull();

            getCalendarEvents();
            initDateItems();

            add(formLayout);
            add(toolBar);
            setHorizontalComponentAlignment(Alignment.CENTER, toolBar);
            add(calendar);
            add(mAddCustomer);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setValuesToSubUnitCombobox(UnitsEntity pUnitEntity) {
        try {
            if (pUnitEntity != null) {
                ComboBox.ItemFilter<UnitsEntity> filter = (unitsEntity, filterString) -> (unitsEntity.getName().toLowerCase().startsWith(filterString.toLowerCase()));
                List<UnitsEntity> subUnitList = mBookingService.getSubUnits(pUnitEntity.getUnitId());
                mSubUnitCombo.setItems(filter, subUnitList);
                mSubUnitCombo.setItemLabelGenerator(units -> units.getName());
            } else {
                ComboBox.ItemFilter<UnitsEntity> filter = (unitsEntity, filterString) -> (unitsEntity.getName().toLowerCase().startsWith(filterString.toLowerCase()));
                mSubUnitCombo.setItems(filter, new ArrayList<UnitsEntity>());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getCalendarEvents() {

        try {

            HashMap<String, String> clientColourMap = new HashMap<>();
            Random rand = new Random(); //instance of random class
            int upperbound = pColourList.size();

            calendar.removeAllEntries();
            Long unitId = null;
            Long parentUnitId = null;

            if (mSubUnitCombo.getValue() != null) {
                unitId = mSubUnitCombo.getValue().getUnitId();
            }

            if (mMainUnitCombo.getValue() != null) {
                parentUnitId = mMainUnitCombo.getValue().getUnitId();
            }

            List<ClientCheckInCalendarEntity> entityList = mBookingService.getCalendarEventsByUnitId(unitId, parentUnitId);

            for (ClientCheckInCalendarEntity entity : entityList) {

                int intRandom = rand.nextInt(upperbound);

                // Create a initial sample entry
                Entry entry = new Entry();
                String title = entity.getClientId().getName() + " : " + parentIdNameMap.get(entity.getUnitId().getParentUnitId()) + " - " + entity.getUnitId().getName();
                entry.setTitle(title);

                if (clientColourMap.get(title) == null) {
                    String colour = pColourList.get(intRandom);
                    entry.setColor(colour);
                    clientColourMap.put(title, colour);
                } else {
                    entry.setColor(clientColourMap.get(title));
                }

                Calendar c = Calendar.getInstance();
                c.setTime(entity.getBookedDate());
                TimeZone tz = c.getTimeZone();
                ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();

                entry.setAllDay(true);
                entry.setStart(LocalDateTime.ofInstant(c.toInstant(), zid));
                entry.setEnd(LocalDateTime.ofInstant(c.toInstant(), zid));

                calendar.addEntry(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDateItems() {
        mMenuBar.addItem(VaadinIcon.ANGLE_LEFT.create(), e -> calendar.previous());

        // simulate the date picker light that we can use in polymer
        DatePicker gotoDate = new DatePicker();
        gotoDate.addValueChangeListener(event1 -> calendar.gotoDate(event1.getValue()));
        gotoDate.getElement().getStyle().set("visibility", "hidden");
        gotoDate.getElement().getStyle().set("position", "fixed");
        gotoDate.setWidth("0px");
        gotoDate.setHeight("0px");
        gotoDate.setWeekNumbersVisible(true);
        buttonDatePicker = new Button(VaadinIcon.CALENDAR.create());
        buttonDatePicker.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        buttonDatePicker.getElement().appendChild(gotoDate.getElement());
        buttonDatePicker.addClickListener(event -> gotoDate.open());
        buttonDatePicker.setWidthFull();
        mMenuBar.addItem(buttonDatePicker);
        mMenuBar.addItem(VaadinIcon.ANGLE_RIGHT.create(), e -> calendar.next());
        mMenuBar.addItem("Today", e -> calendar.today());
    }

    public void updateInterval(LocalDate intervalStart) {
        if (buttonDatePicker != null && selectedView != null) {
            updateIntervalLabel(buttonDatePicker, selectedView, intervalStart);
        }
    }

    void updateIntervalLabel(HasText intervalLabel, CalendarView view, LocalDate intervalStart) {
        String text = "--";
        Locale locale = calendar.getLocale();

        if (view instanceof CalendarViewImpl) {
            switch ((CalendarViewImpl) view) {
                default:
                case DAY_GRID_MONTH:
                case LIST_MONTH:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(locale));
                    break;
                case TIME_GRID_DAY:
                case DAY_GRID_DAY:
                case LIST_DAY:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(locale));
                    break;
                case TIME_GRID_WEEK:
                case DAY_GRID_WEEK:
                case LIST_WEEK:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " - " + intervalStart.plusDays(6).format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " (cw " + intervalStart.format(DateTimeFormatter.ofPattern("ww").withLocale(locale)) + ")";
                    break;
                case LIST_YEAR:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("yyyy").withLocale(locale));
                    break;
            }
        } else if (view instanceof SchedulerView) {
            switch ((SchedulerView) view) {
                case TIMELINE_DAY:
                case RESOURCE_TIMELINE_DAY:
                case RESOURCE_TIME_GRID_DAY:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(locale));
                    break;
                case TIMELINE_WEEK:
                case RESOURCE_TIMELINE_WEEK:
                case RESOURCE_TIME_GRID_WEEK:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " - " + intervalStart.plusDays(6).format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " (cw " + intervalStart.format(DateTimeFormatter.ofPattern("ww").withLocale(locale)) + ")";
                    break;
                case TIMELINE_MONTH:
                case RESOURCE_TIMELINE_MONTH:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(locale));
                    break;
                case TIMELINE_YEAR:
                case RESOURCE_TIMELINE_YEAR:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("yyyy").withLocale(locale));
                    break;
            }
        } else {
            String pattern = view != null && view.getDateTimeFormatPattern() != null ? view.getDateTimeFormatPattern() : "MMMM yyyy";
            text = intervalStart.format(DateTimeFormatter.ofPattern(pattern).withLocale(locale));

        }

        intervalLabel.setText(text);
    }

}