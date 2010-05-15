/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.mobile;

import com.kapti.mobileclient.XmlRpcClientFactory;
import com.kapti.mobileclient.finance.FinanceFactory;
import com.kapti.mobileclient.finance.Security;
import com.kapti.mobileclient.user.Order;
import com.kapti.mobileclient.user.OrderFactory;
import com.kapti.mobileclient.user.User;
import com.kapti.mobileclient.user.UserFactory;
import com.kapti.mobileclient.user.UserSecurity;
import java.util.Vector;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import org.netbeans.microedition.lcdui.LoginScreen;
import org.netbeans.microedition.lcdui.SimpleTableModel;
import org.netbeans.microedition.lcdui.SplashScreen;
import org.netbeans.microedition.lcdui.TableItem;
import org.netbeans.microedition.lcdui.WaitScreen;
import org.netbeans.microedition.util.SimpleCancellableTask;

/**
 * @author Thijs
 */
public class StockPlay extends MIDlet implements CommandListener, ItemCommandListener, Runnable {

    private boolean midletPaused = false;
    private User user = null;
    private Vector portfoliosecurities;
    private Vector orders;
    private Vector cancellableorders;
    //private Security detailSecurity;
    //private Quote detailQuote;
    //private String securitysymbol;
    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private java.util.Hashtable __previousDisplayables = new java.util.Hashtable();
    private Command exitCommand;
    private Command okCommand;
    private Command backCommand;
    private Command itemCommand;
    private Command orderCommand;
    private Command backCommand1;
    private Command backCommand2;
    private Command cancelOrderCommand;
    private Command exitCommand1;
    private Command itemCommand1;
    private Command itemCommand2;
    private Command okCommand1;
    private Command cancelCommand;
    private SplashScreen splashScreen;
    private LoginScreen loginScreen;
    private Form portfolio;
    private TableItem portfolioTableItem;
    private WaitScreen portfoliowaitScreen;
    private WaitScreen verifyCredentialsScreen;
    private Alert wrongCredentialsAlert;
    private Form createOrderForm;
    private TextField createOrderPrice;
    private ChoiceGroup createOrderType;
    private TextField createOrderStockSymbol;
    private DateField createOrderExpiration;
    private TextField createOrderAmount;
    private Alert orderCreatedAlert;
    private WaitScreen createOrderWaitScreen;
    private Form ordersOverviewForm;
    private TableItem tableItem1;
    private ChoiceGroup cancelOrderChoiceGroup;
    private Spacer spacer;
    private Alert orderCreationFailedAlert;
    private WaitScreen ordersWaitScreen;
    private WaitScreen cancelOrderWaitScreen;
    private List mainMenuList;
    private TextBox selectStockForm;
    private WaitScreen loadingStockWaitScreen;
    private Alert orderCancelledAlert;
    private Alert orderCancelFailedAlert;
    private Image logo;
    private SimpleTableModel menuTableModel;
    private SimpleTableModel portfolioTableModel;
    private SimpleCancellableTask PortfolioTask;
    private SimpleCancellableTask verifyOrdersTask;
    private SimpleCancellableTask createOrderTask;
    private SimpleCancellableTask loadOrdersTask;
    private SimpleTableModel ordersTableModel;
    private SimpleCancellableTask cancelOrderTask;
    private SimpleCancellableTask loadStockTask;
    //</editor-fold>//GEN-END:|fields|0|

    /**
     * The HelloMIDlet constructor.
     */
    public StockPlay() {
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    /**
     * Switches a display to previous displayable of the current displayable.
     * The <code>display</code> instance is obtain from the <code>getDisplay</code> method.
     */
    private void switchToPreviousDisplayable() {
        Displayable __currentDisplayable = getDisplay().getCurrent();
        if (__currentDisplayable != null) {
            Displayable __nextDisplayable = (Displayable) __previousDisplayables.get(__currentDisplayable);
            if (__nextDisplayable != null) {
                switchDisplayable(null, __nextDisplayable);
            }
        }
    }
    //</editor-fold>//GEN-END:|methods|0|
    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        switchDisplayable(null, getSplashScreen());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        Displayable __currentDisplayable = display.getCurrent();
        if (__currentDisplayable != null  &&  nextDisplayable != null) {
            __previousDisplayables.put(nextDisplayable, __currentDisplayable);
        }
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == cancelOrderWaitScreen) {//GEN-BEGIN:|7-commandAction|1|111-preAction
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|1|111-preAction
                // write pre-action user code here
                switchDisplayable(getOrderCancelFailedAlert(), getMainMenuList());//GEN-LINE:|7-commandAction|2|111-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|3|110-preAction
                // write pre-action user code here
                switchDisplayable(getOrderCancelledAlert(), getMainMenuList());//GEN-LINE:|7-commandAction|4|110-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|5|74-preAction
        } else if (displayable == createOrderForm) {
            if (command == backCommand1) {//GEN-END:|7-commandAction|5|74-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainMenuList());//GEN-LINE:|7-commandAction|6|74-postAction
                // write post-action user code here
            } else if (command == orderCommand) {//GEN-LINE:|7-commandAction|7|72-preAction
                // write pre-action user code here
                switchDisplayable(null, getCreateOrderWaitScreen());//GEN-LINE:|7-commandAction|8|72-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|83-preAction
        } else if (displayable == createOrderWaitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|9|83-preAction
                // write pre-action user code here
                switchDisplayable(getOrderCreationFailedAlert(), getMainMenuList());//GEN-LINE:|7-commandAction|10|83-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|11|82-preAction
                // write pre-action user code here
                switchDisplayable(getOrderCreatedAlert(), getMainMenuList());//GEN-LINE:|7-commandAction|12|82-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|13|143-preAction
        } else if (displayable == loadingStockWaitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|13|143-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|14|143-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|15|142-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|16|142-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|28-preAction
        } else if (displayable == loginScreen) {
            if (command == LoginScreen.LOGIN_COMMAND) {//GEN-END:|7-commandAction|17|28-preAction
                // write pre-action user code here
                switchDisplayable(null, getVerifyCredentialsScreen());//GEN-LINE:|7-commandAction|18|28-postAction
                // write post-action user code here
            } else if (command == exitCommand) {//GEN-LINE:|7-commandAction|19|32-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|20|32-postAction
                // write post-action user code here
            } else if (command == okCommand) {//GEN-LINE:|7-commandAction|21|34-preAction
                // write pre-action user code here
                switchDisplayable(null, getVerifyCredentialsScreen());//GEN-LINE:|7-commandAction|22|34-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|23|121-preAction
        } else if (displayable == mainMenuList) {
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|23|121-preAction
                // write pre-action user code here
                mainMenuListAction();//GEN-LINE:|7-commandAction|24|121-postAction
                // write post-action user code here
            } else if (command == exitCommand1) {//GEN-LINE:|7-commandAction|25|131-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|26|131-postAction
                // write post-action user code here
            } else if (command == itemCommand1) {//GEN-LINE:|7-commandAction|27|133-preAction
                // write pre-action user code here
                mainMenuListAction();//GEN-LINE:|7-commandAction|28|133-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|29|94-preAction
        } else if (displayable == ordersOverviewForm) {
            if (command == backCommand2) {//GEN-END:|7-commandAction|29|94-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainMenuList());//GEN-LINE:|7-commandAction|30|94-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|31|100-preAction
        } else if (displayable == ordersWaitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|31|100-preAction
                // write pre-action user code here
                switchDisplayable(null, getOrdersOverviewForm());//GEN-LINE:|7-commandAction|32|100-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|33|99-preAction
                // write pre-action user code here
                switchDisplayable(null, getOrdersOverviewForm());//GEN-LINE:|7-commandAction|34|99-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|35|44-preAction
        } else if (displayable == portfolio) {
            if (command == backCommand) {//GEN-END:|7-commandAction|35|44-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainMenuList());//GEN-LINE:|7-commandAction|36|44-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|37|54-preAction
        } else if (displayable == portfoliowaitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|37|54-preAction
                // write pre-action user code here
                switchDisplayable(null, getPortfolio());//GEN-LINE:|7-commandAction|38|54-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|39|53-preAction
                // write pre-action user code here
                switchDisplayable(null, getPortfolio());//GEN-LINE:|7-commandAction|40|53-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|41|149-preAction
        } else if (displayable == selectStockForm) {
            if (command == cancelCommand) {//GEN-END:|7-commandAction|41|149-preAction
                // write pre-action user code here
                switchToPreviousDisplayable();//GEN-LINE:|7-commandAction|42|149-postAction
                // write post-action user code here
            } else if (command == okCommand1) {//GEN-LINE:|7-commandAction|43|146-preAction
                // write pre-action user code here
                switchDisplayable(null, getLoadingStockWaitScreen());//GEN-LINE:|7-commandAction|44|146-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|45|24-preAction
        } else if (displayable == splashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|45|24-preAction
                // write pre-action user code here
                switchDisplayable(null, getLoginScreen());//GEN-LINE:|7-commandAction|46|24-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|47|62-preAction
        } else if (displayable == verifyCredentialsScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|47|62-preAction
                // write pre-action user code here
                switchDisplayable(getWrongCredentialsAlert(), getLoginScreen());//GEN-LINE:|7-commandAction|48|62-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|49|61-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainMenuList());//GEN-LINE:|7-commandAction|50|61-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|51|7-postCommandAction
        }//GEN-END:|7-commandAction|51|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|52|
    //</editor-fold>//GEN-END:|7-commandAction|52|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|18-getter|0|18-preInit
    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {//GEN-END:|18-getter|0|18-preInit
            // write pre-init user code here
            exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|18-getter|1|18-postInit
            // write post-init user code here
        }//GEN-BEGIN:|18-getter|2|
        return exitCommand;
    }
    //</editor-fold>//GEN-END:|18-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: splashScreen ">//GEN-BEGIN:|22-getter|0|22-preInit
    /**
     * Returns an initiliazed instance of splashScreen component.
     * @return the initialized component instance
     */
    public SplashScreen getSplashScreen() {
        if (splashScreen == null) {//GEN-END:|22-getter|0|22-preInit
            // write pre-init user code here
            splashScreen = new SplashScreen(getDisplay());//GEN-BEGIN:|22-getter|1|22-postInit
            splashScreen.setTitle("Loading StockPlay");
            splashScreen.setCommandListener(this);
            splashScreen.setImage(getLogo());
            splashScreen.setText("Please wait while\nStockPlay Mobile is loading");//GEN-END:|22-getter|1|22-postInit
            // write post-init user code here
        }//GEN-BEGIN:|22-getter|2|
        return splashScreen;
    }
    //</editor-fold>//GEN-END:|22-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: loginScreen ">//GEN-BEGIN:|26-getter|0|26-preInit
    /**
     * Returns an initiliazed instance of loginScreen component.
     * @return the initialized component instance
     */
    public LoginScreen getLoginScreen() {
        if (loginScreen == null) {//GEN-END:|26-getter|0|26-preInit
            // write pre-init user code here
            loginScreen = new LoginScreen(getDisplay());//GEN-BEGIN:|26-getter|1|26-postInit
            loginScreen.setLabelTexts("Username", "Password");
            loginScreen.setTitle("StockPlay Login");
            loginScreen.addCommand(LoginScreen.LOGIN_COMMAND);
            loginScreen.addCommand(getExitCommand());
            loginScreen.addCommand(getOkCommand());
            loginScreen.setCommandListener(this);
            loginScreen.setBGColor(-3355444);
            loginScreen.setFGColor(0);
            loginScreen.setPassword("thijs");
            loginScreen.setUsername("thijs");
            loginScreen.setLoginTitle("");
            loginScreen.setUseLoginButton(true);
            loginScreen.setLoginButtonText("Login");//GEN-END:|26-getter|1|26-postInit
            // write post-init user code here
        }//GEN-BEGIN:|26-getter|2|
        return loginScreen;
    }
    //</editor-fold>//GEN-END:|26-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: logo ">//GEN-BEGIN:|30-getter|0|30-preInit
    /**
     * Returns an initiliazed instance of logo component.
     * @return the initialized component instance
     */
    public Image getLogo() {
        if (logo == null) {//GEN-END:|30-getter|0|30-preInit
            // write pre-init user code here
            try {//GEN-BEGIN:|30-getter|1|30-@java.io.IOException
                logo = Image.createImage("/logo.png");
            } catch (java.io.IOException e) {//GEN-END:|30-getter|1|30-@java.io.IOException
                e.printStackTrace();
            }//GEN-LINE:|30-getter|2|30-postInit
            // write post-init user code here
        }//GEN-BEGIN:|30-getter|3|
        return logo;
    }
    //</editor-fold>//GEN-END:|30-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand ">//GEN-BEGIN:|33-getter|0|33-preInit
    /**
     * Returns an initiliazed instance of okCommand component.
     * @return the initialized component instance
     */
    public Command getOkCommand() {
        if (okCommand == null) {//GEN-END:|33-getter|0|33-preInit
            // write pre-init user code here
            okCommand = new Command("Ok", Command.OK, 0);//GEN-LINE:|33-getter|1|33-postInit
            // write post-init user code here
        }//GEN-BEGIN:|33-getter|2|
        return okCommand;
    }
    //</editor-fold>//GEN-END:|33-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: menuTableModel ">//GEN-BEGIN:|39-getter|0|39-preInit
    /**
     * Returns an initiliazed instance of menuTableModel component.
     * @return the initialized component instance
     */
    public SimpleTableModel getMenuTableModel() {
        if (menuTableModel == null) {//GEN-END:|39-getter|0|39-preInit
            // write pre-init user code here
            menuTableModel = new SimpleTableModel(new java.lang.String[][] {//GEN-BEGIN:|39-getter|1|39-postInit
                new java.lang.String[] { "View portfolio" },
                new java.lang.String[] { "View orders" },
                new java.lang.String[] { "Create order" },
                new java.lang.String[] { "View stock" }}, null);//GEN-END:|39-getter|1|39-postInit
            // write post-init user code here
        }//GEN-BEGIN:|39-getter|2|
        return menuTableModel;
    }
    //</editor-fold>//GEN-END:|39-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Items ">//GEN-BEGIN:|17-itemCommandAction|0|17-preItemCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular item.
     * @param command the Command that was invoked
     * @param displayable the Item where the command was invoked
     */
    public void commandAction(Command command, Item item) {//GEN-END:|17-itemCommandAction|0|17-preItemCommandAction
        // write pre-action user code here
        if (item == cancelOrderChoiceGroup) {//GEN-BEGIN:|17-itemCommandAction|1|108-preAction
            if (command == cancelOrderCommand) {//GEN-END:|17-itemCommandAction|1|108-preAction
                // write pre-action user code here
                switchDisplayable(null, getCancelOrderWaitScreen());//GEN-LINE:|17-itemCommandAction|2|108-postAction
                // write post-action user code here
            }//GEN-BEGIN:|17-itemCommandAction|3|153-preAction
        } else if (item == tableItem1) {
            if (command == backCommand1) {//GEN-END:|17-itemCommandAction|3|153-preAction
                // write pre-action user code here
//GEN-LINE:|17-itemCommandAction|4|153-postAction
                // write post-action user code here
            }//GEN-BEGIN:|17-itemCommandAction|5|17-postItemCommandAction
        }//GEN-END:|17-itemCommandAction|5|17-postItemCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|17-itemCommandAction|6|
    //</editor-fold>//GEN-END:|17-itemCommandAction|6|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: itemCommand ">//GEN-BEGIN:|40-getter|0|40-preInit
    /**
     * Returns an initiliazed instance of itemCommand component.
     * @return the initialized component instance
     */
    public Command getItemCommand() {
        if (itemCommand == null) {//GEN-END:|40-getter|0|40-preInit
            // write pre-init user code here
            itemCommand = new Command("Item", Command.ITEM, 0);//GEN-LINE:|40-getter|1|40-postInit
            // write post-init user code here
        }//GEN-BEGIN:|40-getter|2|
        return itemCommand;
    }
    //</editor-fold>//GEN-END:|40-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|43-getter|0|43-preInit
    /**
     * Returns an initiliazed instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {//GEN-END:|43-getter|0|43-preInit
            // write pre-init user code here
            backCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|43-getter|1|43-postInit
            // write post-init user code here
        }//GEN-BEGIN:|43-getter|2|
        return backCommand;
    }
    //</editor-fold>//GEN-END:|43-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: portfolio ">//GEN-BEGIN:|42-getter|0|42-preInit
    /**
     * Returns an initiliazed instance of portfolio component.
     * @return the initialized component instance
     */
    public Form getPortfolio() {
        if (portfolio == null) {//GEN-END:|42-getter|0|42-preInit
            // write pre-init user code here
            portfolio = new Form("Portfolio", new Item[] { getPortfolioTableItem() });//GEN-BEGIN:|42-getter|1|42-postInit
            portfolio.addCommand(getBackCommand());
            portfolio.setCommandListener(this);//GEN-END:|42-getter|1|42-postInit
            // write post-init user code here


        }//GEN-BEGIN:|42-getter|2|
        return portfolio;
    }
    //</editor-fold>//GEN-END:|42-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: portfolioTableItem ">//GEN-BEGIN:|47-getter|0|47-preInit
    /**
     * Returns an initiliazed instance of portfolioTableItem component.
     * @return the initialized component instance
     */
    public TableItem getPortfolioTableItem() {
        if (portfolioTableItem == null) {//GEN-END:|47-getter|0|47-preInit
            // write pre-init user code here
            portfolioTableItem = new TableItem(getDisplay(), "Stocks in portfolio:");//GEN-BEGIN:|47-getter|1|47-postInit
            portfolioTableItem.setModel(getPortfolioTableModel());//GEN-END:|47-getter|1|47-postInit
            // write post-init user code here
        }//GEN-BEGIN:|47-getter|2|
        return portfolioTableItem;
    }
    //</editor-fold>//GEN-END:|47-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: portfolioTableModel ">//GEN-BEGIN:|48-getter|0|48-preInit
    /**
     * Returns an initiliazed instance of portfolioTableModel component.
     * @return the initialized component instance
     */
    public SimpleTableModel getPortfolioTableModel() {
        if (portfolioTableModel == null) {//GEN-END:|48-getter|0|48-preInit
            // write pre-init user code here

            portfolioTableModel = new SimpleTableModel(new java.lang.String[][] {}, new java.lang.String[] { "Stock symbol", "Price", "Change" });//GEN-LINE:|48-getter|1|48-postInit

            if (portfoliosecurities != null) {
                String[][] values = new String[portfoliosecurities.size()][3];

                for (int i = 0; i < portfoliosecurities.size(); i++) {
                    UserSecurity us = (UserSecurity) portfoliosecurities.elementAt(i);
                    values[i][0] = us.getSecurity().getSymbol();
                    values[i][1] = Double.toString(us.getLatestquote().getPrice());

                    double percentage = ((us.getLatestquote().getPrice() / us.getLatestquote().getOpen()) - 1.0) * 100.0;

                    int roundpercentage = (int) (percentage * 100.0);
                    percentage = roundpercentage / 100;



                    values[i][2] = Double.toString(percentage) + "%";

                }

                portfolioTableModel.setValues(values);
            }

            // write post-init user code here
        }//GEN-BEGIN:|48-getter|2|
        return portfolioTableModel;
    }
    //</editor-fold>//GEN-END:|48-getter|2|


    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: portfoliowaitScreen ">//GEN-BEGIN:|50-getter|0|50-preInit
    /**
     * Returns an initiliazed instance of portfoliowaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getPortfoliowaitScreen() {
        if (portfoliowaitScreen == null) {//GEN-END:|50-getter|0|50-preInit
            // write pre-init user code here
            portfoliowaitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|50-getter|1|50-postInit
            portfoliowaitScreen.setTitle("Loading your Portfolio");
            portfoliowaitScreen.setCommandListener(this);
            portfoliowaitScreen.setImage(getLogo());
            portfoliowaitScreen.setText("Please wait while we\'re\nretreiving your portfolio");
            portfoliowaitScreen.setTask(getPortfolioTask());//GEN-END:|50-getter|1|50-postInit
            // write post-init user code here
        }//GEN-BEGIN:|50-getter|2|
        return portfoliowaitScreen;
    }
    //</editor-fold>//GEN-END:|50-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: PortfolioTask ">//GEN-BEGIN:|55-getter|0|55-preInit
    /**
     * Returns an initiliazed instance of PortfolioTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getPortfolioTask() {
        if (PortfolioTask == null) {//GEN-END:|55-getter|0|55-preInit
            // write pre-init user code here
            PortfolioTask = new SimpleCancellableTask();//GEN-BEGIN:|55-getter|1|55-execute
            PortfolioTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|55-getter|1|55-execute
                    // write task-execution user code here
                    portfoliosecurities = UserFactory.getInstance().getPortfolioByUser(user);
                }//GEN-BEGIN:|55-getter|2|55-postInit
            });//GEN-END:|55-getter|2|55-postInit
            // write post-init user code here
        }//GEN-BEGIN:|55-getter|3|
        return PortfolioTask;
    }
    //</editor-fold>//GEN-END:|55-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: verifyCredentialsScreen ">//GEN-BEGIN:|60-getter|0|60-preInit
    /**
     * Returns an initiliazed instance of verifyCredentialsScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getVerifyCredentialsScreen() {
        if (verifyCredentialsScreen == null) {//GEN-END:|60-getter|0|60-preInit
            // write pre-init user code here
            verifyCredentialsScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|60-getter|1|60-postInit
            verifyCredentialsScreen.setTitle("Verifying credentials");
            verifyCredentialsScreen.setCommandListener(this);
            verifyCredentialsScreen.setText("Please wait while we\'re \nverifying your credentials");
            verifyCredentialsScreen.setTask(getVerifyOrdersTask());//GEN-END:|60-getter|1|60-postInit
            // write post-init user code here
        }//GEN-BEGIN:|60-getter|2|
        return verifyCredentialsScreen;
    }
    //</editor-fold>//GEN-END:|60-getter|2|
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: verifyOrdersTask ">//GEN-BEGIN:|63-getter|0|63-preInit
    /**
     * Returns an initiliazed instance of verifyOrdersTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getVerifyOrdersTask() {
        if (verifyOrdersTask == null) {//GEN-END:|63-getter|0|63-preInit
            // write pre-init user code here
            verifyOrdersTask = new SimpleCancellableTask();//GEN-BEGIN:|63-getter|1|63-execute
            verifyOrdersTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|63-getter|1|63-execute
                    // write task-execution user code here

                    UserFactory uf = UserFactory.getInstance();

                    if (!uf.verifyLogin(loginScreen.getUsername(), loginScreen.getPassword())) {
                        throw new Exception("The login was unsuccesful!");
                    } else {

                        user = (User) uf.getUsersByFilter("nickname == '" + loginScreen.getUsername() + "'").firstElement();
                    }

                }//GEN-BEGIN:|63-getter|2|63-postInit
            });//GEN-END:|63-getter|2|63-postInit
            // write post-init user code here
        }//GEN-BEGIN:|63-getter|3|
        return verifyOrdersTask;
    }
    //</editor-fold>//GEN-END:|63-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: wrongCredentialsAlert ">//GEN-BEGIN:|69-getter|0|69-preInit
    /**
     * Returns an initiliazed instance of wrongCredentialsAlert component.
     * @return the initialized component instance
     */
    public Alert getWrongCredentialsAlert() {
        if (wrongCredentialsAlert == null) {//GEN-END:|69-getter|0|69-preInit
            // write pre-init user code here
            wrongCredentialsAlert = new Alert("Wrong Credentials", "The provided credentials are incorrect", null, AlertType.ERROR);//GEN-BEGIN:|69-getter|1|69-postInit
            wrongCredentialsAlert.setTimeout(Alert.FOREVER);//GEN-END:|69-getter|1|69-postInit
            // write post-init user code here
        }//GEN-BEGIN:|69-getter|2|
        return wrongCredentialsAlert;
    }
    //</editor-fold>//GEN-END:|69-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: orderCommand ">//GEN-BEGIN:|71-getter|0|71-preInit
    /**
     * Returns an initiliazed instance of orderCommand component.
     * @return the initialized component instance
     */
    public Command getOrderCommand() {
        if (orderCommand == null) {//GEN-END:|71-getter|0|71-preInit
            // write pre-init user code here
            orderCommand = new Command("Order", Command.OK, 0);//GEN-LINE:|71-getter|1|71-postInit
            // write post-init user code here
        }//GEN-BEGIN:|71-getter|2|
        return orderCommand;
    }
    //</editor-fold>//GEN-END:|71-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand1 ">//GEN-BEGIN:|73-getter|0|73-preInit
    /**
     * Returns an initiliazed instance of backCommand1 component.
     * @return the initialized component instance
     */
    public Command getBackCommand1() {
        if (backCommand1 == null) {//GEN-END:|73-getter|0|73-preInit
            // write pre-init user code here
            backCommand1 = new Command("Back", Command.BACK, 0);//GEN-LINE:|73-getter|1|73-postInit
            // write post-init user code here
        }//GEN-BEGIN:|73-getter|2|
        return backCommand1;
    }
    //</editor-fold>//GEN-END:|73-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: createOrderForm ">//GEN-BEGIN:|70-getter|0|70-preInit
    /**
     * Returns an initiliazed instance of createOrderForm component.
     * @return the initialized component instance
     */
    public Form getCreateOrderForm() {
        if (createOrderForm == null) {//GEN-END:|70-getter|0|70-preInit
            // write pre-init user code here
            createOrderForm = new Form("Order Form", new Item[] { getCreateOrderStockSymbol(), getCreateOrderAmount(), getCreateOrderPrice(), getCreateOrderType(), getCreateOrderExpiration() });//GEN-BEGIN:|70-getter|1|70-postInit
            createOrderForm.addCommand(getOrderCommand());
            createOrderForm.addCommand(getBackCommand1());
            createOrderForm.setCommandListener(this);//GEN-END:|70-getter|1|70-postInit
            // write post-init user code here
        }//GEN-BEGIN:|70-getter|2|
        return createOrderForm;
    }
    //</editor-fold>//GEN-END:|70-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: createOrderStockSymbol ">//GEN-BEGIN:|75-getter|0|75-preInit
    /**
     * Returns an initiliazed instance of createOrderStockSymbol component.
     * @return the initialized component instance
     */
    public TextField getCreateOrderStockSymbol() {
        if (createOrderStockSymbol == null) {//GEN-END:|75-getter|0|75-preInit
            // write pre-init user code here
            createOrderStockSymbol = new TextField("Stock symbol:", null, 32, TextField.ANY);//GEN-LINE:|75-getter|1|75-postInit
            // write post-init user code here
        }//GEN-BEGIN:|75-getter|2|
        return createOrderStockSymbol;
    }
    //</editor-fold>//GEN-END:|75-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: createOrderPrice ">//GEN-BEGIN:|76-getter|0|76-preInit
    /**
     * Returns an initiliazed instance of createOrderPrice component.
     * @return the initialized component instance
     */
    public TextField getCreateOrderPrice() {
        if (createOrderPrice == null) {//GEN-END:|76-getter|0|76-preInit
            // write pre-init user code here
            createOrderPrice = new TextField("Price:", null, 32, TextField.DECIMAL);//GEN-LINE:|76-getter|1|76-postInit
            // write post-init user code here
        }//GEN-BEGIN:|76-getter|2|
        return createOrderPrice;
    }
    //</editor-fold>//GEN-END:|76-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: createOrderType ">//GEN-BEGIN:|77-getter|0|77-preInit
    /**
     * Returns an initiliazed instance of createOrderType component.
     * @return the initialized component instance
     */
    public ChoiceGroup getCreateOrderType() {
        if (createOrderType == null) {//GEN-END:|77-getter|0|77-preInit
            // write pre-init user code here
            createOrderType = new ChoiceGroup("Type:", Choice.EXCLUSIVE);//GEN-BEGIN:|77-getter|1|77-postInit
            createOrderType.append("Buy", null);
            createOrderType.append("Immediate buy", null);
            createOrderType.append("Sell", null);
            createOrderType.append("Immediate sell", null);
            createOrderType.setFitPolicy(Choice.TEXT_WRAP_DEFAULT);
            createOrderType.setSelectedFlags(new boolean[] { true, false, false, false });//GEN-END:|77-getter|1|77-postInit
            // write post-init user code here
        }//GEN-BEGIN:|77-getter|2|
        return createOrderType;
    }
    //</editor-fold>//GEN-END:|77-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: createOrderWaitScreen ">//GEN-BEGIN:|81-getter|0|81-preInit
    /**
     * Returns an initiliazed instance of createOrderWaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getCreateOrderWaitScreen() {
        if (createOrderWaitScreen == null) {//GEN-END:|81-getter|0|81-preInit
            // write pre-init user code here
            createOrderWaitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|81-getter|1|81-postInit
            createOrderWaitScreen.setTitle("waitScreen");
            createOrderWaitScreen.setCommandListener(this);
            createOrderWaitScreen.setTask(getCreateOrderTask());//GEN-END:|81-getter|1|81-postInit
            // write post-init user code here
        }//GEN-BEGIN:|81-getter|2|
        return createOrderWaitScreen;
    }
    //</editor-fold>//GEN-END:|81-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: orderCreatedAlert ">//GEN-BEGIN:|87-getter|0|87-preInit
    /**
     * Returns an initiliazed instance of orderCreatedAlert component.
     * @return the initialized component instance
     */
    public Alert getOrderCreatedAlert() {
        if (orderCreatedAlert == null) {//GEN-END:|87-getter|0|87-preInit
            // write pre-init user code here
            orderCreatedAlert = new Alert("Order Created", "Your order was succesfully created", null, AlertType.CONFIRMATION);//GEN-BEGIN:|87-getter|1|87-postInit
            orderCreatedAlert.setTimeout(Alert.FOREVER);//GEN-END:|87-getter|1|87-postInit
            // write post-init user code here
        }//GEN-BEGIN:|87-getter|2|
        return orderCreatedAlert;
    }
    //</editor-fold>//GEN-END:|87-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: orderCreationFailedAlert ">//GEN-BEGIN:|89-getter|0|89-preInit
    /**
     * Returns an initiliazed instance of orderCreationFailedAlert component.
     * @return the initialized component instance
     */
    public Alert getOrderCreationFailedAlert() {
        if (orderCreationFailedAlert == null) {//GEN-END:|89-getter|0|89-preInit
            // write pre-init user code here
            orderCreationFailedAlert = new Alert("Order creation failed", "An error occured while placing the order.. \nPlease check your values and try again.", null, AlertType.ERROR);//GEN-BEGIN:|89-getter|1|89-postInit
            orderCreationFailedAlert.setTimeout(Alert.FOREVER);//GEN-END:|89-getter|1|89-postInit
            // write post-init user code here
        }//GEN-BEGIN:|89-getter|2|
        return orderCreationFailedAlert;
    }
    //</editor-fold>//GEN-END:|89-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: createOrderTask ">//GEN-BEGIN:|84-getter|0|84-preInit
    /**
     * Returns an initiliazed instance of createOrderTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getCreateOrderTask() {
        if (createOrderTask == null) {//GEN-END:|84-getter|0|84-preInit
            // write pre-init user code here
            createOrderTask = new SimpleCancellableTask();//GEN-BEGIN:|84-getter|1|84-execute
            createOrderTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|84-getter|1|84-execute
                    // write task-execution user code here


                    String symbol = createOrderStockSymbol.getString().toUpperCase();

                    Vector securities = (Vector) FinanceFactory.getInstance().getSecuritiesByFilter("symbol == '" + symbol + "'");


                    if(securities.isEmpty())
                        throw new Exception("No security with that symbol found!");

                    if(getCreateOrderAmount().getString().length() == 0)
                        throw new Exception("No amount given to order!");
                    if(getCreateOrderPrice().getString().length() == 0)
                        throw new Exception("No price given to order");


                    int amount = Integer.parseInt(getCreateOrderAmount().getString());
                    double price = Double.parseDouble(getCreateOrderPrice().getString());

                    Order o = OrderFactory.getInstance().createOrder();
                    o.setAmount(amount);
                    o.setSecurity((Security) securities.firstElement());
                    o.setPrice(price);
                    o.setUser(user);

                    switch (getCreateOrderType().getSelectedIndex()) {
                        case 0:
                            o.setType(Order.Type.BUY);
                            break;
                        case 1:
                            o.setType(Order.Type.IMMEDIATE_BUY);
                            break;
                    }

                    o.setExpirationTime(createOrderExpiration.getDate());

                    if (!OrderFactory.getInstance().makePersistent(o)) {
                        throw new Exception("Saving the order failed!");
                    }


                }//GEN-BEGIN:|84-getter|2|84-postInit
            });//GEN-END:|84-getter|2|84-postInit
            // write post-init user code here
        }//GEN-BEGIN:|84-getter|3|
        return createOrderTask;
    }
    //</editor-fold>//GEN-END:|84-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand2 ">//GEN-BEGIN:|93-getter|0|93-preInit
    /**
     * Returns an initiliazed instance of backCommand2 component.
     * @return the initialized component instance
     */
    public Command getBackCommand2() {
        if (backCommand2 == null) {//GEN-END:|93-getter|0|93-preInit
            // write pre-init user code here
            backCommand2 = new Command("Back", Command.BACK, 0);//GEN-LINE:|93-getter|1|93-postInit
            // write post-init user code here
        }//GEN-BEGIN:|93-getter|2|
        return backCommand2;
    }
    //</editor-fold>//GEN-END:|93-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ordersOverviewForm ">//GEN-BEGIN:|92-getter|0|92-preInit
    /**
     * Returns an initiliazed instance of ordersOverviewForm component.
     * @return the initialized component instance
     */
    public Form getOrdersOverviewForm() {
        if (ordersOverviewForm == null) {//GEN-END:|92-getter|0|92-preInit
            // write pre-init user code here
            ordersOverviewForm = new Form("Orders overview", new Item[] { getTableItem1(), getSpacer(), getCancelOrderChoiceGroup() });//GEN-BEGIN:|92-getter|1|92-postInit
            ordersOverviewForm.addCommand(getBackCommand2());
            ordersOverviewForm.setCommandListener(this);//GEN-END:|92-getter|1|92-postInit
            // write post-init user code here
        }//GEN-BEGIN:|92-getter|2|
        return ordersOverviewForm;
    }
    //</editor-fold>//GEN-END:|92-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: tableItem1 ">//GEN-BEGIN:|96-getter|0|96-preInit
    /**
     * Returns an initiliazed instance of tableItem1 component.
     * @return the initialized component instance
     */
    public TableItem getTableItem1() {
        if (tableItem1 == null) {//GEN-END:|96-getter|0|96-preInit
            // write pre-init user code here
            tableItem1 = new TableItem(getDisplay(), "Orders:");//GEN-BEGIN:|96-getter|1|96-postInit
            tableItem1.addCommand(getBackCommand1());
            tableItem1.setItemCommandListener(this);
            tableItem1.setTitle("<null>");
            tableItem1.setModel(getOrdersTableModel());//GEN-END:|96-getter|1|96-postInit
            // write post-init user code here
        }//GEN-BEGIN:|96-getter|2|
        return tableItem1;
    }
    //</editor-fold>//GEN-END:|96-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ordersTableModel ">//GEN-BEGIN:|97-getter|0|97-preInit
    /**
     * Returns an initiliazed instance of ordersTableModel component.
     * @return the initialized component instance
     */
    public SimpleTableModel getOrdersTableModel() {
        if (ordersTableModel == null) {//GEN-END:|97-getter|0|97-preInit
            // write pre-init user code here
            ordersTableModel = new SimpleTableModel(new java.lang.String[][] {}, new java.lang.String[] { "Symbol", "Price", "Type" });//GEN-LINE:|97-getter|1|97-postInit
            // write post-init user code here

            if (orders != null) {

                String[][] values = new String[orders.size()][4];
                getCancelOrderChoiceGroup().deleteAll();
                cancellableorders = new Vector();;

                for (int i = 0; i < orders.size(); i++) {
                    Order o = (Order) orders.elementAt(i);

                    values[i][0] = o.getSecurity().getSymbol();
                    values[i][1] = o.getPrice().toString();
                    values[i][2] = o.getType();
                    values[i][3] = o.getStatus();




                    if (!o.getStatus().equals(Order.OrderStatus.EXECUTED) && !o.getStatus().equals(Order.OrderStatus.FAILED)) {
                        getCancelOrderChoiceGroup().append(o.getSecurity().getSymbol(), null);
                        cancellableorders.addElement(o);

                    }
                }

                ordersTableModel.setValues(values);

            }


        }//GEN-BEGIN:|97-getter|2|
        return ordersTableModel;
    }
    //</editor-fold>//GEN-END:|97-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ordersWaitScreen ">//GEN-BEGIN:|98-getter|0|98-preInit
    /**
     * Returns an initiliazed instance of ordersWaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getOrdersWaitScreen() {
        if (ordersWaitScreen == null) {//GEN-END:|98-getter|0|98-preInit
            // write pre-init user code here
            ordersWaitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|98-getter|1|98-postInit
            ordersWaitScreen.setTitle("Orders loading");
            ordersWaitScreen.setCommandListener(this);
            ordersWaitScreen.setTask(getLoadOrdersTask());//GEN-END:|98-getter|1|98-postInit
            // write post-init user code here
        }//GEN-BEGIN:|98-getter|2|
        return ordersWaitScreen;
    }
    //</editor-fold>//GEN-END:|98-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: loadOrdersTask ">//GEN-BEGIN:|101-getter|0|101-preInit
    /**
     * Returns an initiliazed instance of loadOrdersTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getLoadOrdersTask() {
        if (loadOrdersTask == null) {//GEN-END:|101-getter|0|101-preInit
            // write pre-init user code here
            loadOrdersTask = new SimpleCancellableTask();//GEN-BEGIN:|101-getter|1|101-execute
            loadOrdersTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|101-getter|1|101-execute
                    // write task-execution user code here

                    orders = OrderFactory.getInstance().getOrdersByFilter("userid == '" + user.getId() + "'");
                }//GEN-BEGIN:|101-getter|2|101-postInit
            });//GEN-END:|101-getter|2|101-postInit
            // write post-init user code here
        }//GEN-BEGIN:|101-getter|3|
        return loadOrdersTask;
    }
    //</editor-fold>//GEN-END:|101-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelOrderCommand ">//GEN-BEGIN:|107-getter|0|107-preInit
    /**
     * Returns an initiliazed instance of cancelOrderCommand component.
     * @return the initialized component instance
     */
    public Command getCancelOrderCommand() {
        if (cancelOrderCommand == null) {//GEN-END:|107-getter|0|107-preInit
            // write pre-init user code here
            cancelOrderCommand = new Command("Ok", "Cancel Order", Command.OK, 0);//GEN-LINE:|107-getter|1|107-postInit
            // write post-init user code here
        }//GEN-BEGIN:|107-getter|2|
        return cancelOrderCommand;
    }
    //</editor-fold>//GEN-END:|107-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: spacer ">//GEN-BEGIN:|104-getter|0|104-preInit
    /**
     * Returns an initiliazed instance of spacer component.
     * @return the initialized component instance
     */
    public Spacer getSpacer() {
        if (spacer == null) {//GEN-END:|104-getter|0|104-preInit
            // write pre-init user code here
            spacer = new Spacer(16, 1);//GEN-LINE:|104-getter|1|104-postInit
            // write post-init user code here
        }//GEN-BEGIN:|104-getter|2|
        return spacer;
    }
    //</editor-fold>//GEN-END:|104-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelOrderChoiceGroup ">//GEN-BEGIN:|106-getter|0|106-preInit
    /**
     * Returns an initiliazed instance of cancelOrderChoiceGroup component.
     * @return the initialized component instance
     */
    public ChoiceGroup getCancelOrderChoiceGroup() {
        if (cancelOrderChoiceGroup == null) {//GEN-END:|106-getter|0|106-preInit
            // write pre-init user code here
            cancelOrderChoiceGroup = new ChoiceGroup("Cancel an order:", Choice.EXCLUSIVE);//GEN-BEGIN:|106-getter|1|106-postInit
            cancelOrderChoiceGroup.addCommand(getCancelOrderCommand());
            cancelOrderChoiceGroup.setItemCommandListener(this);
            cancelOrderChoiceGroup.setDefaultCommand(getCancelOrderCommand());//GEN-END:|106-getter|1|106-postInit
            // write post-init user code here
        }//GEN-BEGIN:|106-getter|2|
        return cancelOrderChoiceGroup;
    }
    //</editor-fold>//GEN-END:|106-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelOrderWaitScreen ">//GEN-BEGIN:|109-getter|0|109-preInit
    /**
     * Returns an initiliazed instance of cancelOrderWaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getCancelOrderWaitScreen() {
        if (cancelOrderWaitScreen == null) {//GEN-END:|109-getter|0|109-preInit
            // write pre-init user code here
            cancelOrderWaitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|109-getter|1|109-postInit
            cancelOrderWaitScreen.setTitle("Cancelling order...");
            cancelOrderWaitScreen.setCommandListener(this);
            cancelOrderWaitScreen.setText("Please wait while your order is being cancelled");
            cancelOrderWaitScreen.setTask(getCancelOrderTask());//GEN-END:|109-getter|1|109-postInit
            // write post-init user code here
        }//GEN-BEGIN:|109-getter|2|
        return cancelOrderWaitScreen;
    }
    //</editor-fold>//GEN-END:|109-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelOrderTask ">//GEN-BEGIN:|112-getter|0|112-preInit
    /**
     * Returns an initiliazed instance of cancelOrderTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getCancelOrderTask() {
        if (cancelOrderTask == null) {//GEN-END:|112-getter|0|112-preInit
            // write pre-init user code here
            cancelOrderTask = new SimpleCancellableTask();//GEN-BEGIN:|112-getter|1|112-execute
            cancelOrderTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|112-getter|1|112-execute
                    // write task-execution user code here

                    //ophalen geselecteerde order
                    Order o = (Order) cancellableorders.elementAt(getCancelOrderChoiceGroup().getSelectedIndex());
                    o.setStatus(Order.OrderStatus.CANCELLED);

                    OrderFactory.getInstance().makePersistent(o);



                }//GEN-BEGIN:|112-getter|2|112-postInit
            });//GEN-END:|112-getter|2|112-postInit
            // write post-init user code here
        }//GEN-BEGIN:|112-getter|3|
        return cancelOrderTask;
    }
    //</editor-fold>//GEN-END:|112-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: createOrderAmount ">//GEN-BEGIN:|116-getter|0|116-preInit
    /**
     * Returns an initiliazed instance of createOrderAmount component.
     * @return the initialized component instance
     */
    public TextField getCreateOrderAmount() {
        if (createOrderAmount == null) {//GEN-END:|116-getter|0|116-preInit
            // write pre-init user code here
            createOrderAmount = new TextField("Amount:", "1", 32, TextField.NUMERIC);//GEN-LINE:|116-getter|1|116-postInit
            // write post-init user code here
        }//GEN-BEGIN:|116-getter|2|
        return createOrderAmount;
    }
    //</editor-fold>//GEN-END:|116-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: createOrderExpiration ">//GEN-BEGIN:|117-getter|0|117-preInit
    /**
     * Returns an initiliazed instance of createOrderExpiration component.
     * @return the initialized component instance
     */
    public DateField getCreateOrderExpiration() {
        if (createOrderExpiration == null) {//GEN-END:|117-getter|0|117-preInit
            // write pre-init user code here
            createOrderExpiration = new DateField("Expiration date:", DateField.DATE_TIME, java.util.TimeZone.getTimeZone("Europe/Brussels"));//GEN-BEGIN:|117-getter|1|117-postInit
            createOrderExpiration.setDate(new java.util.Date(System.currentTimeMillis() + 1000*60*60*24*31 ));//GEN-END:|117-getter|1|117-postInit
            // write post-init user code here
        }//GEN-BEGIN:|117-getter|2|
        return createOrderExpiration;
    }
    //</editor-fold>//GEN-END:|117-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand1 ">//GEN-BEGIN:|130-getter|0|130-preInit
    /**
     * Returns an initiliazed instance of exitCommand1 component.
     * @return the initialized component instance
     */
    public Command getExitCommand1() {
        if (exitCommand1 == null) {//GEN-END:|130-getter|0|130-preInit
            // write pre-init user code here
            exitCommand1 = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|130-getter|1|130-postInit
            // write post-init user code here
        }//GEN-BEGIN:|130-getter|2|
        return exitCommand1;
    }
    //</editor-fold>//GEN-END:|130-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: mainMenuList ">//GEN-BEGIN:|119-getter|0|119-preInit
    /**
     * Returns an initiliazed instance of mainMenuList component.
     * @return the initialized component instance
     */
    public List getMainMenuList() {
        if (mainMenuList == null) {//GEN-END:|119-getter|0|119-preInit
            // write pre-init user code here
            mainMenuList = new List("Main Menu", Choice.IMPLICIT);//GEN-BEGIN:|119-getter|1|119-postInit
            mainMenuList.append("View Portfolio", null);
            mainMenuList.append("View orders", null);
            mainMenuList.append("Create order", null);
            mainMenuList.addCommand(getExitCommand1());
            mainMenuList.addCommand(getItemCommand1());
            mainMenuList.setCommandListener(this);
            mainMenuList.setSelectedFlags(new boolean[] { true, false, false });//GEN-END:|119-getter|1|119-postInit
            // write post-init user code here
        }//GEN-BEGIN:|119-getter|2|
        return mainMenuList;
    }
    //</editor-fold>//GEN-END:|119-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: mainMenuListAction ">//GEN-BEGIN:|119-action|0|119-preAction
    /**
     * Performs an action assigned to the selected list element in the mainMenuList component.
     */
    public void mainMenuListAction() {//GEN-END:|119-action|0|119-preAction
        // enter pre-action user code here
        String __selectedString = getMainMenuList().getString(getMainMenuList().getSelectedIndex());//GEN-BEGIN:|119-action|1|123-preAction
        if (__selectedString != null) {
            if (__selectedString.equals("View Portfolio")) {//GEN-END:|119-action|1|123-preAction
                // write pre-action user code here
                switchDisplayable(null, getPortfoliowaitScreen());//GEN-LINE:|119-action|2|123-postAction
                // write post-action user code here
            } else if (__selectedString.equals("View orders")) {//GEN-LINE:|119-action|3|124-preAction
                // write pre-action user code here
                switchDisplayable(null, getOrdersWaitScreen());//GEN-LINE:|119-action|4|124-postAction
                // write post-action user code here
            } else if (__selectedString.equals("Create order")) {//GEN-LINE:|119-action|5|125-preAction
                // write pre-action user code here
                switchDisplayable(null, getCreateOrderForm());//GEN-LINE:|119-action|6|125-postAction
                // write post-action user code here
            }//GEN-BEGIN:|119-action|7|119-postAction
        }//GEN-END:|119-action|7|119-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|119-action|8|
    //</editor-fold>//GEN-END:|119-action|8|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: itemCommand1 ">//GEN-BEGIN:|132-getter|0|132-preInit
    /**
     * Returns an initiliazed instance of itemCommand1 component.
     * @return the initialized component instance
     */
    public Command getItemCommand1() {
        if (itemCommand1 == null) {//GEN-END:|132-getter|0|132-preInit
            // write pre-init user code here
            itemCommand1 = new Command("Select", Command.ITEM, 1);//GEN-LINE:|132-getter|1|132-postInit
            // write post-init user code here
        }//GEN-BEGIN:|132-getter|2|
        return itemCommand1;
    }
    //</editor-fold>//GEN-END:|132-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: itemCommand2 ">//GEN-BEGIN:|134-getter|0|134-preInit
    /**
     * Returns an initiliazed instance of itemCommand2 component.
     * @return the initialized component instance
     */
    public Command getItemCommand2() {
        if (itemCommand2 == null) {//GEN-END:|134-getter|0|134-preInit
            // write pre-init user code here
            itemCommand2 = new Command("Item", Command.ITEM, 0);//GEN-LINE:|134-getter|1|134-postInit
            // write post-init user code here
        }//GEN-BEGIN:|134-getter|2|
        return itemCommand2;
    }
    //</editor-fold>//GEN-END:|134-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: selectStockForm ">//GEN-BEGIN:|139-getter|0|139-preInit
    /**
     * Returns an initiliazed instance of selectStockForm component.
     * @return the initialized component instance
     */
    public TextBox getSelectStockForm() {
        if (selectStockForm == null) {//GEN-END:|139-getter|0|139-preInit
            // write pre-init user code here
            selectStockForm = new TextBox("Select Stock:", null, 15, TextField.ANY | TextField.INITIAL_CAPS_WORD);//GEN-BEGIN:|139-getter|1|139-postInit
            selectStockForm.addCommand(getOkCommand1());
            selectStockForm.addCommand(getCancelCommand());
            selectStockForm.setCommandListener(this);//GEN-END:|139-getter|1|139-postInit
            // write post-init user code here
        }//GEN-BEGIN:|139-getter|2|
        return selectStockForm;
    }
    //</editor-fold>//GEN-END:|139-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: loadingStockWaitScreen ">//GEN-BEGIN:|141-getter|0|141-preInit
    /**
     * Returns an initiliazed instance of loadingStockWaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getLoadingStockWaitScreen() {
        if (loadingStockWaitScreen == null) {//GEN-END:|141-getter|0|141-preInit
            // write pre-init user code here
            loadingStockWaitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|141-getter|1|141-postInit
            loadingStockWaitScreen.setTitle("Stock info is loading..");
            loadingStockWaitScreen.setCommandListener(this);
            loadingStockWaitScreen.setText("Please wait while the stock\n information is being loaded");
            loadingStockWaitScreen.setTask(getLoadStockTask());//GEN-END:|141-getter|1|141-postInit
            // write post-init user code here
        }//GEN-BEGIN:|141-getter|2|
        return loadingStockWaitScreen;
    }
    //</editor-fold>//GEN-END:|141-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: loadStockTask ">//GEN-BEGIN:|144-getter|0|144-preInit
    /**
     * Returns an initiliazed instance of loadStockTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getLoadStockTask() {
        if (loadStockTask == null) {//GEN-END:|144-getter|0|144-preInit
            // write pre-init user code here
            loadStockTask = new SimpleCancellableTask();//GEN-BEGIN:|144-getter|1|144-execute
            loadStockTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|144-getter|1|144-execute
                    // write task-execution user code here
                    //Vector result = FinanceFactory.getInstance().getSecuritiesByFilter("symbol == '" + securitysymbol +"'");
                    //detailSecurity = (Security) result.firstElement();
                    //detailLatestQuote = (Quote) FinanceFactory.getInstance().getLatestQuoteFromSecurity(detailSecurity);
                }//GEN-BEGIN:|144-getter|2|144-postInit
            });//GEN-END:|144-getter|2|144-postInit
            // write post-init user code here
        }//GEN-BEGIN:|144-getter|3|
        return loadStockTask;
    }
    //</editor-fold>//GEN-END:|144-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand1 ">//GEN-BEGIN:|145-getter|0|145-preInit
    /**
     * Returns an initiliazed instance of okCommand1 component.
     * @return the initialized component instance
     */
    public Command getOkCommand1() {
        if (okCommand1 == null) {//GEN-END:|145-getter|0|145-preInit
            // write pre-init user code here
            okCommand1 = new Command("Ok", Command.OK, 0);//GEN-LINE:|145-getter|1|145-postInit
            // write post-init user code here
        }//GEN-BEGIN:|145-getter|2|
        return okCommand1;
    }
    //</editor-fold>//GEN-END:|145-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelCommand ">//GEN-BEGIN:|148-getter|0|148-preInit
    /**
     * Returns an initiliazed instance of cancelCommand component.
     * @return the initialized component instance
     */
    public Command getCancelCommand() {
        if (cancelCommand == null) {//GEN-END:|148-getter|0|148-preInit
            // write pre-init user code here
            cancelCommand = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|148-getter|1|148-postInit
            // write post-init user code here
        }//GEN-BEGIN:|148-getter|2|
        return cancelCommand;
    }
    //</editor-fold>//GEN-END:|148-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: orderCancelledAlert ">//GEN-BEGIN:|154-getter|0|154-preInit
    /**
     * Returns an initiliazed instance of orderCancelledAlert component.
     * @return the initialized component instance
     */
    public Alert getOrderCancelledAlert() {
        if (orderCancelledAlert == null) {//GEN-END:|154-getter|0|154-preInit
            // write pre-init user code here
            orderCancelledAlert = new Alert("Order cancelled", "The order was succesfully cancelled", null, null);//GEN-BEGIN:|154-getter|1|154-postInit
            orderCancelledAlert.setTimeout(2500);//GEN-END:|154-getter|1|154-postInit
            // write post-init user code here
        }//GEN-BEGIN:|154-getter|2|
        return orderCancelledAlert;
    }
    //</editor-fold>//GEN-END:|154-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: orderCancelFailedAlert ">//GEN-BEGIN:|156-getter|0|156-preInit
    /**
     * Returns an initiliazed instance of orderCancelFailedAlert component.
     * @return the initialized component instance
     */
    public Alert getOrderCancelFailedAlert() {
        if (orderCancelFailedAlert == null) {//GEN-END:|156-getter|0|156-preInit
            // write pre-init user code here
            orderCancelFailedAlert = new Alert("Order Cancel Failed", "The order couldn\'t be cancelled. Try again later", null, null);//GEN-BEGIN:|156-getter|1|156-postInit
            orderCancelFailedAlert.setTimeout(Alert.FOREVER);//GEN-END:|156-getter|1|156-postInit
            // write post-init user code here
        }//GEN-BEGIN:|156-getter|2|
        return orderCancelFailedAlert;
    }
    //</editor-fold>//GEN-END:|156-getter|2|

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
        XmlRpcClientFactory.resetSession();
    }

    public void run() {
    }
}
