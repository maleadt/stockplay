/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.mobile;

import java.util.Vector;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import org.netbeans.microedition.lcdui.LoginScreen;
import org.netbeans.microedition.lcdui.SimpleTableModel;
import org.netbeans.microedition.lcdui.SplashScreen;
import org.netbeans.microedition.lcdui.TableItem;
import org.kxmlrpc.*;
import org.netbeans.microedition.lcdui.WaitScreen;
import org.netbeans.microedition.util.SimpleCancellableTask;

/**
 * @author Thijs
 */
public class StockPlay extends MIDlet implements CommandListener, ItemCommandListener, Runnable {

    private boolean midletPaused = false;
    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command exitCommand;
    private Command okCommand;
    private Command backCommand;
    private Command itemCommand;
    private Form menu;
    private StringItem stringItem;
    private ImageItem imageItem;
    private TableItem tableItem;
    private SplashScreen splashScreen;
    private LoginScreen loginScreen;
    private Form portfolio;
    private TableItem portfolioTableItem;
    private TextField textField;
    private WaitScreen waitScreen;
    private Image logo;
    private SimpleTableModel menuTableModel;
    private SimpleTableModel portfolioTableModel;
    private SimpleCancellableTask PortfolioTask;
    //</editor-fold>//GEN-END:|fields|0|

    /**
     * The HelloMIDlet constructor.
     */
    public StockPlay() {
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|
    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
        stringItem = new StringItem("", "Choose an option:", Item.PLAIN);//GEN-BEGIN:|0-initialize|1|0-postInitialize
        stringItem.setLayout(ImageItem.LAYOUT_DEFAULT);//GEN-END:|0-initialize|1|0-postInitialize
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
        if (displayable == loginScreen) {//GEN-BEGIN:|7-commandAction|1|28-preAction
            if (command == LoginScreen.LOGIN_COMMAND) {//GEN-END:|7-commandAction|1|28-preAction
                // write pre-action user code here
                switchDisplayable(null, getMenu());//GEN-LINE:|7-commandAction|2|28-postAction
                // write post-action user code here
            } else if (command == exitCommand) {//GEN-LINE:|7-commandAction|3|32-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|4|32-postAction
                // write post-action user code here
            } else if (command == okCommand) {//GEN-LINE:|7-commandAction|5|34-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|6|34-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|7|19-preAction
        } else if (displayable == menu) {
            if (command == exitCommand) {//GEN-END:|7-commandAction|7|19-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|8|19-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|44-preAction
        } else if (displayable == portfolio) {
            if (command == backCommand) {//GEN-END:|7-commandAction|9|44-preAction
                // write pre-action user code here
                switchDisplayable(null, getMenu());//GEN-LINE:|7-commandAction|10|44-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|11|24-preAction
        } else if (displayable == splashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|11|24-preAction
                // write pre-action user code here
                switchDisplayable(null, getLoginScreen());//GEN-LINE:|7-commandAction|12|24-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|13|54-preAction
        } else if (displayable == waitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|13|54-preAction
                // write pre-action user code here
                switchDisplayable(null, getPortfolio());//GEN-LINE:|7-commandAction|14|54-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|15|53-preAction
                // write pre-action user code here
                switchDisplayable(null, getPortfolio());//GEN-LINE:|7-commandAction|16|53-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|7-postCommandAction
        }//GEN-END:|7-commandAction|17|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|18|
    //</editor-fold>//GEN-END:|7-commandAction|18|

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

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: menu ">//GEN-BEGIN:|14-getter|0|14-preInit
    /**
     * Returns an initiliazed instance of menu component.
     * @return the initialized component instance
     */
    public Form getMenu() {
        if (menu == null) {//GEN-END:|14-getter|0|14-preInit
            // write pre-init user code here
            menu = new Form("StockPlay Menu", new Item[] { getImageItem(), stringItem, getTableItem() });//GEN-BEGIN:|14-getter|1|14-postInit
            menu.addCommand(getExitCommand());
            menu.setCommandListener(this);//GEN-END:|14-getter|1|14-postInit
            // write post-init user code here
        }//GEN-BEGIN:|14-getter|2|
        return menu;
    }
    //</editor-fold>//GEN-END:|14-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: splashScreen ">//GEN-BEGIN:|22-getter|0|22-preInit
    /**
     * Returns an initiliazed instance of splashScreen component.
     * @return the initialized component instance
     */
    public SplashScreen getSplashScreen() {
        if (splashScreen == null) {//GEN-END:|22-getter|0|22-preInit
            // write pre-init user code here
            splashScreen = new SplashScreen(getDisplay());//GEN-BEGIN:|22-getter|1|22-postInit
            splashScreen.setTitle("splashScreen");
            splashScreen.setCommandListener(this);
            splashScreen.setImage(getLogo());//GEN-END:|22-getter|1|22-postInit
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

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: imageItem ">//GEN-BEGIN:|35-getter|0|35-preInit
    /**
     * Returns an initiliazed instance of imageItem component.
     * @return the initialized component instance
     */
    public ImageItem getImageItem() {
        if (imageItem == null) {//GEN-END:|35-getter|0|35-preInit
            // write pre-init user code here
            imageItem = new ImageItem("", getLogo(), ImageItem.LAYOUT_CENTER, "<Missing Image>", Item.PLAIN);//GEN-LINE:|35-getter|1|35-postInit
            // write post-init user code here
        }//GEN-BEGIN:|35-getter|2|
        return imageItem;
    }
    //</editor-fold>//GEN-END:|35-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: tableItem ">//GEN-BEGIN:|38-getter|0|38-preInit
    /**
     * Returns an initiliazed instance of tableItem component.
     * @return the initialized component instance
     */
    public TableItem getTableItem() {
        if (tableItem == null) {//GEN-END:|38-getter|0|38-preInit
            // write pre-init user code here
            tableItem = new TableItem(getDisplay(), "");//GEN-BEGIN:|38-getter|1|38-postInit
            tableItem.addCommand(getItemCommand());
            tableItem.setItemCommandListener(this);
            tableItem.setModel(getMenuTableModel());//GEN-END:|38-getter|1|38-postInit
            // write post-init user code here
        }//GEN-BEGIN:|38-getter|2|
        return tableItem;
    }
    //</editor-fold>//GEN-END:|38-getter|2|

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
        if (item == tableItem) {//GEN-BEGIN:|17-itemCommandAction|1|41-preAction
            if (command == itemCommand) {//GEN-END:|17-itemCommandAction|1|41-preAction
                // write pre-action user code here
                switchDisplayable(null, getWaitScreen());//GEN-LINE:|17-itemCommandAction|2|41-postAction
                // write post-action user code here
            }//GEN-BEGIN:|17-itemCommandAction|3|17-postItemCommandAction
        }//GEN-END:|17-itemCommandAction|3|17-postItemCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|17-itemCommandAction|4|
    //</editor-fold>//GEN-END:|17-itemCommandAction|4|

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
            portfolio = new Form("Portfolio", new Item[] { getPortfolioTableItem(), getTextField() });//GEN-BEGIN:|42-getter|1|42-postInit
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
            portfolioTableModel = new SimpleTableModel(new java.lang.String[][] {//GEN-BEGIN:|48-getter|1|48-postInit
                new java.lang.String[] { "CSCO", "25.87", "+0.25%" },
                new java.lang.String[] { "MSFT", "90", "-5.24%" }}, new java.lang.String[] { "Stock symbol", "Price", "Change" });//GEN-END:|48-getter|1|48-postInit
            // write post-init user code here
        }//GEN-BEGIN:|48-getter|2|
        return portfolioTableModel;
    }
    //</editor-fold>//GEN-END:|48-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField ">//GEN-BEGIN:|49-getter|0|49-preInit
    /**
     * Returns an initiliazed instance of textField component.
     * @return the initialized component instance
     */
    public TextField getTextField() {
        if (textField == null) {//GEN-END:|49-getter|0|49-preInit
            // write pre-init user code here
            textField = new TextField("TEST", null, 32, TextField.ANY);//GEN-LINE:|49-getter|1|49-postInit
            // write post-init user code here
        }//GEN-BEGIN:|49-getter|2|
        return textField;
    }
    //</editor-fold>//GEN-END:|49-getter|2|
    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: waitScreen ">//GEN-BEGIN:|50-getter|0|50-preInit
    /**
     * Returns an initiliazed instance of waitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getWaitScreen() {
        if (waitScreen == null) {//GEN-END:|50-getter|0|50-preInit
            // write pre-init user code here
            waitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|50-getter|1|50-postInit
            waitScreen.setTitle("Loading your Portfolio");
            waitScreen.setCommandListener(this);
            waitScreen.setImage(getLogo());
            waitScreen.setText("Please wait while we\'re\nretreiving your portfolio");
            waitScreen.setTask(getPortfolioTask());//GEN-END:|50-getter|1|50-postInit
            // write post-init user code here
        }//GEN-BEGIN:|50-getter|2|
        return waitScreen;
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
                    XmlRpcClient client = new XmlRpcClient("http://localhost:8081");

                    Vector parameters = new Vector();
                    parameters.addElement(new Integer(2));
                    parameters.addElement(new Integer(3));

                    try{
                    Object result = client.execute("math.add", parameters);
                    getTextField().setString(((Integer) result).toString());
                    }catch(Exception ex){
                        ex.printStackTrace();
                        textField.setString(ex.getMessage());
                    }
                }//GEN-BEGIN:|55-getter|2|55-postInit
            });//GEN-END:|55-getter|2|55-postInit
            // write post-init user code here
        }//GEN-BEGIN:|55-getter|3|
        return PortfolioTask;
    }
    //</editor-fold>//GEN-END:|55-getter|3|

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
    }

    public void run() {
    }
}
