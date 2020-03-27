package com.infosys.outlook;

import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import com.infosys.outlook.contoso.Authentication;
import com.infosys.outlook.contoso.Graph;
import com.microsoft.graph.models.extensions.DateTimeTimeZone;
import com.microsoft.graph.models.extensions.Event;
import com.microsoft.graph.models.extensions.User;



public class App 
{
    public static void main(String[] args) {
        System.out.println("Java Graph Tutorial");
        System.out.println();
        
        // Load OAuth settings
        final Properties oAuthProperties = new Properties();
        try {
            oAuthProperties.load(App.class.getClassLoader().getResourceAsStream("oAuth.properties"));
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Unable to read OAuth configuration. Make sure you have a properly formatted oAuth.properties file. See README for details.");
            return;
        }

        final String appId = oAuthProperties.getProperty("app.id");
        final String[] appScopes = oAuthProperties.getProperty("app.scopes").split(",");
        
        
     // Get an access token
        Authentication.initialize(appId);
        final String accessToken = Authentication.getUserAccessToken(appScopes);
        
        //display token
        System.out.println("Access token: " + accessToken);
        
     // Greet the user
        User user = Graph.getUser(accessToken);
        System.out.println("Welcome " + user.displayName);
        System.out.println();

        Scanner input = new Scanner(System.in);

        int choice = -1;

        while (choice != 0) {
            System.out.println("Please choose one of the following options:");
            System.out.println("0. Exit");
            System.out.println("1. Display access token");
            System.out.println("2. List calendar events");

            try {
                choice = input.nextInt();
            } catch (InputMismatchException ex) {
                // Skip over non-integer input
                input.nextLine();
            }

            // Process user choice
            switch(choice) {
                case 0:
                    // Exit the program
                    System.out.println("Goodbye...");
                    break;
                case 1:
                    // Display access token
                    break;
                case 2:
                    // List the calendar
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            listCalendarEvents(accessToken);
        }

        input.close();
    }
    
    private static String formatDateTimeTimeZone(DateTimeTimeZone date) {
        LocalDateTime dateTime = LocalDateTime.parse(date.dateTime);

        return dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + " (" + date.timeZone + ")";
    }
    
    private static void listCalendarEvents(String accessToken) {
        // Get the user's events
        List<Event> events = Graph.getEvents(accessToken);

        System.out.println("Events:");

        for (Event event : events) {
            System.out.println("Subject: " + event.subject);
            System.out.println("  Organizer: " + event.organizer.emailAddress.name);
            System.out.println("  Start: " + formatDateTimeTimeZone(event.start));
            System.out.println("  End: " + formatDateTimeTimeZone(event.end));
        }

        System.out.println();
    }
}
