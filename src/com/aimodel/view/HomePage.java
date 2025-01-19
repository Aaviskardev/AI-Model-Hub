


package com.aimodel.view;

import com.aimodel.model.AiModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;


/**
 *
 * @author Aaviskar  23048648
 */

/**
 * HomePage class represents the main window of the AI Model application.
 * It uses CardLayout to manage navigation between different panels.
 */

public class HomePage extends JFrame {
    // UI Components
    private JPanel mainPanel;
    private JButton aboutUsButton;
    private JButton viewModelButton;
    private JButton loginButton;
    private JLabel customNeedLabel;
    private JLabel chooseModelLabel;
    private JLabel brandLogoLabel;
    private JLabel backgroundLabel;
    private JPanel loginPanel;
    private final JFXPanel fxPanel; // For embedding JavaFX content
    private JPanel dashboardPanel;
    private final JFXPanel dashboardFXPanel; // For embedding JavaFX content in the dashboard
    private AiModel aiModel;
    private JPanel viewModelsPanel;
    private final JFXPanel viewModelsFXPanel; // For embedding JavaFX content in the view models panel
    private AboutUsPanel aboutUsPanel;

    // Layout and Navigation
    private CardLayout cardLayout;
    private NavigationHandlerImpl navigationHandler;

    /**
     * NavigationHandler interface defines the contract for navigating between different sections.
     */
    public interface NavigationHandler {
        void navigateTo(String section);
    }

    /**
     * NavigationHandlerImpl implements the NavigationHandler to handle panel switching using CardLayout.
     */
    public class NavigationHandlerImpl implements NavigationHandler {
        @Override
        public void navigateTo(String section) {
            cardLayout.show(getContentPane(), section);
        }
    }

    /**
     * Constructs the HomePage, initializes UI components, and sets up the main application frame.
     */
    public HomePage() {
        fxPanel = new JFXPanel();
        dashboardFXPanel = new JFXPanel();
        viewModelsFXPanel = new JFXPanel();

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        navigationHandler = new NavigationHandlerImpl();

        initializeComponents();
        initLoginPanel();
        initAiModel();
        initAboutUsPanel();
        initDashboardPanel();
        initViewModelsPanel();

        add(mainPanel, "main");
        add(loginPanel, "login");
        add(dashboardPanel, "dashboard");
        add(viewModelsPanel, "viewModels");
        add(aboutUsPanel, "aboutUs");
    }

    /**
     * Initializes the main UI components of the HomePage.
     */
    private void initializeComponents() {
        setTitle("AI Model - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));
        setPreferredSize(new Dimension(1000, 600));
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        mainPanel.setPreferredSize(new Dimension(1000, 600));

        aboutUsButton = new JButton("About Us");
        aboutUsButton.setFont(new Font("Arial", Font.BOLD, 12));
        aboutUsButton.setContentAreaFilled(false);
        aboutUsButton.setFocusPainted(false);
        aboutUsButton.addActionListener(this::aboutUsButtonActionPerformed);
        mainPanel.add(aboutUsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 20, -1, 30));

        viewModelButton = new JButton("View Model");
        viewModelButton.setFont(new Font("Arial", Font.BOLD, 12));
        viewModelButton.setContentAreaFilled(false);
        viewModelButton.addActionListener(this::viewModelButtonActionPerformed);
        mainPanel.add(viewModelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 20, -1, 30));

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(115, 115, 222));
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this::loginButtonActionPerformed);
        mainPanel.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 20, -1, 30));

        customNeedLabel = new JLabel("For Your Custom Need");
        customNeedLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        customNeedLabel.setForeground(new Color(102, 102, 102));
        mainPanel.add(customNeedLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, -1, 60));

        chooseModelLabel = new JLabel("Choose Best AI Model");
        chooseModelLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        chooseModelLabel.setForeground(new Color(102, 102, 102));
        mainPanel.add(chooseModelLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 190, -1, -1));

        ImageIcon brandLogoIcon = new ImageIcon(getClass().getResource("/com/aimodel/resources/brandlogo.png"));
        brandLogoLabel = new JLabel(brandLogoIcon);
        mainPanel.add(brandLogoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 70));

        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/com/aimodel/resources/homepage.png"));
        backgroundLabel = new JLabel(backgroundIcon);
        mainPanel.add(backgroundLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 600));
    }

    /**
     * Initializes the login panel, which contains a JavaFX-based login form.
     */
    private void initLoginPanel() {
        loginPanel = new JPanel(new BorderLayout());
        loginPanel.setPreferredSize(new Dimension(1000, 600));

        fxPanel.setPreferredSize(new Dimension(1000, 600));
        loginPanel.add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            FXLoginPane loginPane = new FXLoginPane(this::handleLoginSuccess);
            Scene scene = loginPane.createScene();
            fxPanel.setScene(scene);
        });

        loginPanel.setVisible(false);
    }

    /**
     * Initializes the AiModel, which holds the data for AI models.
     */
    private void initAiModel() {
        aiModel = new AiModel();
    }

    /**
     * Initializes the About Us panel.
     */
    private void initAboutUsPanel() {
        aboutUsPanel = new AboutUsPanel();
        aboutUsPanel.setNavigationHandler(navigationHandler);
    }

    /**
     * Initializes the dashboard panel, which is shown after a successful admin login.
     */
    private void initDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setPreferredSize(new Dimension(1000, 600));

        dashboardFXPanel.setPreferredSize(new Dimension(1000, 600));
        dashboardPanel.add(dashboardFXPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            AdminDashboardPane dashboardPane = new AdminDashboardPane(this::handleLogout, aiModel, navigationHandler);

            // Set the listener to refresh ViewModelsPane when a model is updated
            dashboardPane.setModelUpdateListener(() -> {
                Platform.runLater(() -> {
                    if (viewModelsPane != null) {
                        viewModelsPane.refreshModels();
                    }
                });
            });

            Scene scene = dashboardPane.createScene();
            dashboardFXPanel.setScene(scene);
        });

        dashboardPanel.setVisible(false);
    }

    /**
     * Initializes the View Models panel, which displays a list of AI models.
     */
    private ViewModelsPane viewModelsPane;

    private void initViewModelsPanel() {
        viewModelsPanel = new JPanel(new BorderLayout());
        viewModelsPanel.setPreferredSize(new Dimension(1000, 600));

        viewModelsFXPanel.setPreferredSize(new Dimension(1000, 600));
        viewModelsPanel.add(viewModelsFXPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            viewModelsPane = new ViewModelsPane(aiModel, navigationHandler);
            Scene scene = viewModelsPane.createScene();
            viewModelsFXPanel.setScene(scene);
        });

        viewModelsPanel.setVisible(false);
    }

    /**
     * Handles the successful login event. Navigates to either the dashboard (for admin) or the main panel.
     *
     * @param userType The type of the user (e.g., "admin").
     */
    private void handleLoginSuccess(String userType) {
        SwingUtilities.invokeLater(() -> {
            if ("admin".equals(userType)) {
                navigationHandler.navigateTo("dashboard");
            } else {
                navigationHandler.navigateTo("main");
            }
            loginPanel.setVisible(false);
        });
    }

    /**
     * Handles the logout event. Navigates back to the main panel.
     */
    private void handleLogout() {
        SwingUtilities.invokeLater(() -> {
            navigationHandler.navigateTo("main");
        });
    }

    /**
     * Action performed when the login button is clicked.
     *
     * @param evt The ActionEvent triggered by the button click.
     */
    private void loginButtonActionPerformed(ActionEvent evt) {
        navigationHandler.navigateTo("login");
        loginPanel.setVisible(true);
    }

    /**
     * Action performed when the "About Us" button is clicked.
     *
     * @param evt The ActionEvent triggered by the button click.
     */
    private void aboutUsButtonActionPerformed(ActionEvent evt) {
        navigationHandler.navigateTo("aboutUs");
        System.out.println("About Us clicked");
    }

    /**
     * Action performed when the "View Model" button is clicked.
     *
     * @param evt The ActionEvent triggered by the button click.
     */
    private void viewModelButtonActionPerformed(ActionEvent evt) {
        navigationHandler.navigateTo("viewModels");
    }

    /**
     * The main method to run the HomePage application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {

            HomePage homePage = new HomePage();
            homePage.setVisible(true);
        
    }
}
