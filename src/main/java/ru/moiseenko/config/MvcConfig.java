package ru.moiseenko.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/mainClient").setViewName("mainClient");
        registry.addViewController("/adminPanel").setViewName("adminPanel");
        registry.addViewController("/companyPanel").setViewName("companyPanel");
        registry.addViewController("/account").setViewName("account");
        registry.addViewController("/accountNotification").setViewName("accountNotification");
        registry.addViewController("/adminPanel/adminUser").setViewName("adminUser");
        registry.addViewController("/adminPanel/adminUser/editAdminUser").setViewName("editAdminUser");
        registry.addViewController("/adminPanel/adminUser/viewAdminUser").setViewName("viewAdminUser");
        registry.addViewController("/adminPanel/adminTrain").setViewName("adminTrain");
        registry.addViewController("/adminPanel/adminTrain/editAdminTrain").setViewName("editAdminTrain");
        registry.addViewController("/adminPanel/adminTrain/viewAdminTrain").setViewName("viewAdminTrain");
        registry.addViewController("/adminPanel/adminTrain/createAdminTrain").setViewName("createAdminTrain");
        registry.addViewController("/adminPanel/adminVan").setViewName("adminVan");
        registry.addViewController("/adminPanel/adminVan/editAdminVan").setViewName("editAdminVan");
        registry.addViewController("/adminPanel/adminVan/viewAdminVan").setViewName("viewAdminVan");
        registry.addViewController("/adminPanel/adminVan/createAdminVan").setViewName("createAdminVan");
        registry.addViewController("/adminPanel/adminCompartment").setViewName("adminCompartment");
        registry.addViewController("/adminPanel/adminCompartment/editAdminCompartment").setViewName("editAdminCompartment");
        registry.addViewController("/adminPanel/adminCompartment/viewAdminCompartment").setViewName("viewAdminCompartment");
        registry.addViewController("/adminPanel/adminCompartment/createAdminCompartment").setViewName("createAdminCompartment");
        registry.addViewController("/adminPanel/adminPlace").setViewName("adminPlace");
        registry.addViewController("/adminPanel/adminPlace/editAdminPlace").setViewName("editAdminPlace");
        registry.addViewController("/adminPanel/adminPlace/viewAdminPlace").setViewName("viewAdminPlace");
        registry.addViewController("/adminPanel/adminPlace/createAdminPlace").setViewName("createAdminPlace");
        registry.addViewController("/adminPanel/adminNotification").setViewName("adminNotification");
        registry.addViewController("/adminPanel/adminNotification/editAdminNotification").setViewName("editAdminNotification");
        registry.addViewController("/adminPanel/adminNotification/viewAdminNotification").setViewName("viewAdminNotification");
        registry.addViewController("/adminPanel/adminNotification/createAdminNotification").setViewName("createAdminNotification");
        registry.addViewController("/adminPanel/adminTicket").setViewName("adminTicket");
        registry.addViewController("/adminPanel/adminTicket/editAdminTicket").setViewName("editAdminTicket");
        registry.addViewController("/adminPanel/adminTicket/viewAdminTicket").setViewName("viewAdminTicket");
        registry.addViewController("/adminPanel/adminTicket/createAdminTicket").setViewName("createAdminTicket");
        registry.addViewController("/adminPanel/adminOrder").setViewName("adminOrder");
        registry.addViewController("/adminPanel/adminOrder/viewAdminOrder").setViewName("viewAdminOrder");
        registry.addViewController("/adminPanel/adminCountry").setViewName("adminCountry");
        registry.addViewController("/adminPanel/adminCountry/editAdminCountry").setViewName("editAdminCountry");
        registry.addViewController("/adminPanel/adminCountry/viewAdminCountry").setViewName("viewAdminCountry");
        registry.addViewController("/adminPanel/adminCountry/createAdminCountry").setViewName("createAdminCountry");
        registry.addViewController("/adminPanel/adminTown").setViewName("adminTown");
        registry.addViewController("/adminPanel/adminTown/editAdminTown").setViewName("editAdminTown");
        registry.addViewController("/adminPanel/adminTown/viewAdminTown").setViewName("viewAdminTown");
        registry.addViewController("/adminPanel/adminTown/createAdminTown").setViewName("createAdminTown");
        registry.addViewController("/adminPanel/adminTerminal").setViewName("adminTerminal");
        registry.addViewController("/adminPanel/adminTerminal/editAdminTerminal").setViewName("editAdminTerminal");
        registry.addViewController("/adminPanel/adminTerminal/viewAdminTerminal").setViewName("viewAdminTerminal");
        registry.addViewController("/adminPanel/adminTerminal/createAdminTerminal").setViewName("createAdminTerminal");
        registry.addViewController("/adminPanel/adminArrival").setViewName("adminArrival");
        registry.addViewController("/adminPanel/adminArrival/editAdminArrival").setViewName("editAdminArrival");
        registry.addViewController("/adminPanel/adminArrival/viewAdminArrival").setViewName("viewAdminArrival");
        registry.addViewController("/adminPanel/adminArrival/createAdminArrival").setViewName("createAdminArrival");
        registry.addViewController("/mainClient/scheduleTrain").setViewName("scheduleTrain");
        registry.addViewController("/mainClient/scheduleTrain/scheduleArrival").setViewName("scheduleArrival");
        registry.addViewController("/mainClient/scheduleTerminal").setViewName("scheduleTerminal");
        registry.addViewController("/mainClient/scheduleTerminal/scheduleTrainArrival").setViewName("scheduleTrainArrival");
        registry.addViewController("/mainClient/ticketArrival").setViewName("ticketArrival");
        registry.addViewController("/companyPanel/companyTerminal").setViewName("companyTerminal");
        registry.addViewController("/companyPanel/companyTerminal/viewCompanyTerminal").setViewName("viewCompanyTerminal");
        registry.addViewController("/companyPanel/companyTerminal/companyArrival").setViewName("companyArrival");
        registry.addViewController("/companyPanel/companyTerminal/companyArrival/viewCompanyArrival").setViewName("viewCompanyArrival");
        registry.addViewController("/companyPanel/companyTerminal/companyArrival/createCompanyArrival").setViewName("createCompanyArrival");
        registry.addViewController("/companyPanel/companyTicket").setViewName("companyTicket");
        registry.addViewController("/companyPanel/companyTicket/viewCompanyTicket").setViewName("viewCompanyTicket");
        registry.addViewController("/companyPanel/companyTicket/editCompanyTicket").setViewName("editCompanyTicket");
        registry.addViewController("/companyPanel/companyTicket/createCompanyTicket").setViewName("createCompanyTicket");
        registry.addViewController("/companyPanel/companyTrain").setViewName("companyTrain");
        registry.addViewController("/companyPanel/companyTrain/viewCompanyTrain").setViewName("viewCompanyTrain");
        registry.addViewController("/companyPanel/companyTrain/createCompanyTrain").setViewName("createCompanyTrain");
        registry.addViewController("/companyPanel/companyVan").setViewName("companyVan");
        registry.addViewController("/companyPanel/companyVan/viewCompanyVan").setViewName("viewCompanyVan");
        registry.addViewController("/companyPanel/companyVan/editCompanyVan").setViewName("editCompanyVan");
        registry.addViewController("/companyPanel/companyVan/createCompanyVan").setViewName("createCompanyVan");
        registry.addViewController("/companyPanel/companyCompartment").setViewName("companyCompartment");
        registry.addViewController("/companyPanel/companyCompartment/viewCompanyCompartment").setViewName("viewCompanyCompartment");
        registry.addViewController("/companyPanel/companyPlace").setViewName("companyPlace");
        registry.addViewController("/companyPanel/companyPlace/viewCompanyPlace").setViewName("viewCompanyPlace");
        registry.addViewController("/mainClient/buyTerminal").setViewName("buyTerminal");
        registry.addViewController("/mainClient/buyVan").setViewName("buyVan");
        registry.addViewController("/mainClient/buyPlace").setViewName("buyPlace");
        registry.addViewController("/mainClient/buyInfo").setViewName("buyInfo");
        registry.addViewController("/mainClient/buyCreditCard").setViewName("buyCreditCard");
        registry.addViewController("/mainClient/myTicket").setViewName("myTicket");
        registry.addViewController("/mainClient/myTicket/viewMyTicket").setViewName("viewMyTicket");
        registry.addViewController("/mainClient/myTicket/deleteMyTicket").setViewName("deleteMyTicket");
    }
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
    }
}
