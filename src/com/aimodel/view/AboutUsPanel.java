package com.aimodel.view;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Aaviskar  23048648
 */
/**
 * AboutUsPanel is a JPanel that displays information about the application.
 * It includes navigation buttons and a brief description of the application's purpose.
 */
public class AboutUsPanel extends JPanel {
    private JButton aboutUsButton;
    private JButton viewModelButton;
    private JButton loginButton;
    private JLabel brandLogo;
    private JPanel contentPanel;
    private JLabel backgroundLabel;
    private HomePage.NavigationHandler navigationHandler;

    /**
     * Constructor for AboutUsPanel.
     * Initializes the layout and components of the panel.
     */
    public AboutUsPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 600));
        initializeComponents();
    }

    /**
     * Sets the navigation handler for the panel.
     *
     * @param handler The NavigationHandler to be used for navigation actions.
     */
    public void setNavigationHandler(HomePage.NavigationHandler handler) {
        this.navigationHandler = handler;
        setupNavigationActions();
    }

    /**
     * Sets up the navigation actions for the buttons.
     * Associates each button with a specific navigation action.
     */
    private void setupNavigationActions() {
        if (navigationHandler != null) {
            // Navigate to "aboutUs" when aboutUsButton is clicked
            aboutUsButton.addActionListener(e -> navigationHandler.navigateTo("aboutUs"));
            // Navigate to "viewModels" when viewModelButton is clicked
            viewModelButton.addActionListener(e -> navigationHandler.navigateTo("viewModels"));
            // Navigate to "login" when loginButton is clicked
            loginButton.addActionListener(e -> navigationHandler.navigateTo("login"));
        }
    }

    /**
     * Initializes the components of the AboutUsPanel.
     * This includes setting up the layered pane, content panel, brand logo, navigation buttons,
     * main heading, subheading, and background GIF.
     */
    private void initializeComponents() {
        // Create a layered pane to hold the background and content
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(1000, 600));
        layeredPane.setBackground(new Color(255, 255, 255));

        // Create a content panel to hold the components
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setOpaque(false);
        contentPanel.setBounds(0, 0, 1000, 600);
        contentPanel.setBackground(new Color(255, 255, 255));

        // Brand logo
        ImageIcon brandLogoIcon = new ImageIcon(getClass().getResource("/com/aimodel/resources/brandlogo.png"));
        brandLogo = new JLabel(brandLogoIcon);
        brandLogo.setBounds(7, 15, 100, 70);
        contentPanel.add(brandLogo);

        // Navigation buttons
        aboutUsButton = createNavButton("About Us");
        aboutUsButton.setBounds(690, 20, 95, 30);
        contentPanel.add(aboutUsButton);

        viewModelButton = createNavButton("View Model");
        viewModelButton.setBounds(790, 20, 105, 30);
        contentPanel.add(viewModelButton);

        loginButton = createLoginButton();
        loginButton.setBounds(900, 20, 75, 30);
        contentPanel.add(loginButton);

        // Main heading - "We collect data on different AI models"
        JLabel mainHeading = new JLabel("We collect data on different AI models.");
        mainHeading.setFont(new Font("Arial", Font.BOLD, 20));
        mainHeading.setForeground(new Color(51, 51, 51));
        mainHeading.setBounds(99, 110, 400, 30); // Further adjusted X and Y
        contentPanel.add(mainHeading);

        // Subheading - "Saving cost, time and compute"
        JLabel subHeading = new JLabel("Saving cost, time and compute.");
        subHeading.setFont(new Font("Arial", Font.BOLD, 20));
        subHeading.setForeground(new Color(51, 51, 51));
        subHeading.setBounds(600, 380, 300, 30); // Further adjusted X and Y
        contentPanel.add(subHeading);

        // Background GIF
        try {
            ImageIcon gifIcon = new ImageIcon(getClass().getResource("/com/aimodel/resources/aboutus.gif.gif"));
            backgroundLabel = new JLabel(gifIcon);
            backgroundLabel.setBounds(0, 0, 1000, 600);
            layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        } catch (Exception e) {
            System.err.println("Error loading GIF: " + e.getMessage());
        }

        // Add the content panel to the layered pane
        layeredPane.add(contentPanel, JLayeredPane.PALETTE_LAYER);
        // Add the layered pane to the main panel
        add(layeredPane, BorderLayout.CENTER);
    }

    /**
     * Creates a navigation button with the specified text.
     *
     * @param text The text to be displayed on the button.
     * @return A JButton configured as a navigation button.
     */
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setContentAreaFilled(false); // Transparent background
        button.setFocusPainted(false); // No focus border
        button.setBorderPainted(false); // No border
        return button;
    }

    /**
     * Creates the login button.
     *
     * @return A JButton configured as the login button.
     */
    private JButton createLoginButton() {
        JButton button = new JButton("Login");
        button.setBackground(new Color(115, 115, 222)); // Background color
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE); // Text color
        button.setOpaque(true); // Opaque background
        button.setBorderPainted(false); // No border
        return button;
    }
}