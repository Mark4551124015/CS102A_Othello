package stage.scene;

import component.animation.Animation;
import component.animation.Animator;
import input.Controller;
import main.PlayerManager;
import main.mainApp;
import newData.Vct;
import object.GUI.Buttons.MenuButton;
import object.GUI.Buttons.NormalButton;
import object.GUI.Buttons.SelectModeButton;
import object.GUI.Menu.ChoosingMenu;
import stage.GameStage;

import object.OthelloObject;
import graphics.Image;
import util.FontLib;

import java.awt.*;

import static java.awt.event.KeyEvent.VK_ESCAPE;
import static main.GameManager.playerManager;

public class Lobby extends OthelloObject implements GameStage {
    //数据部分
    public static final double PopOutDuration = 0.2;
    public static final double PopBackDuration = 0.2;

    public static final double MenuButtonWidth = 280;
    public static final double MenuButtonHeight = 60;
    public static final Vct MainManuButtonsPivot = new Vct(MenuButtonWidth * 0.43, mainApp.Height * 0.30);
    public static final double MenuShift = MenuButtonWidth;
    private boolean exitFlag;
    private boolean help;
    private boolean options;
    private boolean local;
    private boolean online;
    private boolean ai;
    private int Level;

//数据部分结束

    //背景
    private OthelloObject background;
    //背景结束


    private enum MenuState {
        Main, SelectMode, SelectCom, Connect, Help, Room, Empty, SelectAI
    }

    private MenuState menuState;

    //MainMenu部分
    private OthelloObject menu_main;
    private MenuButton button_main_start;
    private MenuButton button_main_help;
    private MenuButton button_main_options;
    private MenuButton button_main_exit;
    private Animator menu_main_animator;
    private Animator menu_main_alphaAnimator;
    //SelectModeMenu部分
    private OthelloObject menu_SelectMode;
    private SelectModeButton button_SelectMode_local;
    private SelectModeButton button_SelectMode_online;
    private SelectModeButton button_SelectMode_ai;
    private NormalButton button_SelectMode_back;
    private Animator menu_SelectMode_animator;
    private Animator menu_SelectMode_alphaAnimator;
    //ChooseCompetitor部分
    private OthelloObject menu_ChooseCom;
    private OthelloObject ChooseBox;
    private ChoosingMenu choosingMenu;
    private Animator menu_ChooseCom_animator;
    private Animator menu_ChooseCom_alphaAnimator;

    //ChooseAi
    private OthelloObject menu_ChooseAI;
    private SelectModeButton button_ChooseAI_Easy;
    private SelectModeButton button_ChooseAI_Normal;
    private SelectModeButton button_ChooseAI_Hard;
    private Animator menu_ChooseAI_animator;
    private Animator menu_ChooseAI_alphaAnimator;

    private boolean isReadyForEsc;

    public Lobby() {
        super("scene_lobby");
    }

    @Override
    public void init() {
        //背景布部分
        this.background = new OthelloObject("background", new Image("background"));
        this.addObj(this.background);
        this.background.resizeTo(mainApp.WinSize);
        this.background.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);

        this.initMainMenu();
        this.initModeMenu();
        this.initAIMenu();
        this.initChoosingMenu();
        this.menu_main_setActive(true);
        this.menu_main_popOut(0);
        isReadyForEsc=true;
        this.menuState = MenuState.Main;
        this.Level = -1;
    }

    @Override
    public void update(double dt) {

        this.menu_main_update(dt);
        this.menu_SelectMode_update(dt);
        this.menu_ChooseCom_update(dt);
        this.setMenu_ChooseAI_update(dt);

        if (this.menuState == MenuState.Main) { // MainTest
            if (this.button_main_start.isClicked()) {
                System.out.println("Play被点击");
                this.changeMenuState(MenuState.SelectMode);
            }
            if (this.button_main_exit.isClicked()) {
                this.exitFlag = true;
            }
            if (this.button_main_help.isClicked()) {
                this.help = true;
            }
            if (this.button_main_options.isClicked()) {
                this.options = true;
            }
        }

        if(this.menuState == MenuState.SelectMode){
            if(this.button_SelectMode_local.isClicked()){
                this.changeMenuState(MenuState.SelectCom);
            }
            if(this.button_SelectMode_online.isClicked()){
                this.online = true;
            }
            if(this.button_SelectMode_ai.isClicked()){
                this.changeMenuState(MenuState.SelectAI);
            }
            if(this.button_SelectMode_back.isClicked() || (Controller.isKeyDown(VK_ESCAPE) && isReadyForEsc)){
                this.changeMenuState(MenuState.Main);
                isReadyForEsc=false;
            }
        }
        if(this.menuState == MenuState.SelectAI){
            if(this.button_ChooseAI_Easy.isClicked()){
                this.ai = true;
                this.Level = 1;
            }
            if(this.button_ChooseAI_Normal.isClicked()){
                this.ai = true;
                this.Level = 2;
            }
            if(this.button_ChooseAI_Hard.isClicked()){
                this.ai = true;
                this.Level = 3;
            }
            if(Controller.isKeyDown(VK_ESCAPE) && isReadyForEsc){
                this.changeMenuState(MenuState.SelectMode);
                isReadyForEsc=false;
            }
        }

        if(this.menuState == MenuState.SelectCom){
            if (this.choosingMenu.isSubmitted()) {
                if (this.choosingMenu.getResult().equals(PlayerManager.User.getUsername())) {
                    this.choosingMenu.clearChoice();
                } else {
                    try {
                        PlayerManager.Competitor = playerManager.getPlayer(this.choosingMenu.getResult());
                        this.local = true;
                    } catch (Exception e) {
                        System.out.println("Invalid Player");
                        this.choosingMenu.setSubmitted(false);
                    }
                }
            }
            if(Controller.isKeyDown(VK_ESCAPE)){
                this.changeMenuState(MenuState.SelectMode);
                isReadyForEsc=false;
            }

            if(this.button_SelectMode_back.isClicked()){
                this.changeMenuState(MenuState.SelectMode);
            }
        }
        if(!Controller.isKeyDown(VK_ESCAPE)&& !isReadyForEsc){
            isReadyForEsc=true;
        }
        super.update(dt);






    }

    @Override
    public GameStageID getGameStageID() {
        return GameStageID.Lobby;
    }



    //主界面开始
    public void initMainMenu() {
        this.menu_main = new OthelloObject("menu_main");
        this.addObj(this.menu_main);
        this.menu_main.setPosition(MainManuButtonsPivot);
        this.menu_main.setAlpha(0);

        this.button_main_start = new MenuButton("menu_main_start",null);
        this.menu_main.addObj(this.button_main_start);
        this.button_main_help = new MenuButton("menu_main_help",null);
        this.menu_main.addObj(this.button_main_help);
        this.button_main_options = new MenuButton("menu_main_options", null);
        this.menu_main.addObj(this.button_main_options);
        this.button_main_exit = new MenuButton("menu_main_exit", null);
        this.menu_main.addObj(this.button_main_exit);

        //大小
        this.button_main_start.resizeTo(MenuButtonWidth, MenuButtonHeight);
        this.button_main_help.resizeTo(MenuButtonWidth, MenuButtonHeight-7);
        this.button_main_options.resizeTo(MenuButtonWidth, MenuButtonHeight-7);
        this.button_main_exit.resizeTo(MenuButtonWidth, MenuButtonHeight-7);

        //位置
        this.button_main_start.setPosition(0, 50);
        this.button_main_help.setPosition(0, 125);
        this.button_main_options.setPosition(0, 180);
        this.button_main_exit.setPosition(0, 235);


        //文字显示
        Font font1 = FontLib.GetMenuButtonFont(30);
        this.button_main_start.setFont(font1);
        Font font2 = FontLib.GetMenuButtonFont(15);
        this.button_main_help.setFont(font2);
        this.button_main_options.setFont(font2);
        this.button_main_exit.setFont(font2);

        this.button_main_start.setText("Play    ");
        this.button_main_help.setText("Help              ");
        this.button_main_options.setText("Options         ");
        this.button_main_exit.setText("Exit Game      ");

        this.button_main_start.setTextColor(new Color(212, 212, 212));
        this.button_main_help.setTextColor(new Color(212, 212, 212));
        this.button_main_options.setTextColor(new Color(212, 212, 212));
        this.button_main_exit.setTextColor(new Color(212, 212, 212));


        this.menu_main_animator = new Animator(0);
        this.menu_main_alphaAnimator = new Animator(0);
        this.addComponent(this.menu_main_animator);
        this.addComponent(this.menu_main_alphaAnimator);

        this.menu_main_setActive(true);
    }

    private void menu_main_popOut(double delay) {
        this.menu_main_animator.forceAppend(Animation.GetSmooth(-MenuShift, 0, PopOutDuration, delay));
        this.menu_main_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_main_alphaAnimator.val(), 1, PopOutDuration, delay));
    }

    private void menu_main_popBack(double delay) {
        this.menu_main_animator.forceAppend(Animation.GetSmooth(this.menu_main_animator.val(), -MenuShift, PopBackDuration, delay));
        this.menu_main_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_main_alphaAnimator.val(), 0, PopBackDuration, delay));
    }

    private void menu_main_update(double dt) {
        this.menu_main.setPosition(MainManuButtonsPivot.x + this.menu_main_animator.val(), MainManuButtonsPivot.y);
        this.menu_main.setAlpha(this.menu_main_alphaAnimator.val());
    }

    private void menu_main_setActive(boolean flag) {
        this.button_main_start.setActive(flag);
        this.button_main_help.setActive(flag);
        this.button_main_options.setActive(flag);
        this.button_main_exit.setActive(flag);
    }
    //主界面结束



    public boolean isExiting() {
        return exitFlag;
    }

    public boolean isHelping() {
        return help;
    }

    public boolean isOptions() {
        return options;
    }


    public void initAIMenu() {
        this.menu_ChooseAI = new OthelloObject("menu_ChooseAI");
        this.addObj(this.menu_ChooseAI);
        this.menu_ChooseAI.setPosition(mainApp.Width/2,mainApp.Height/2);

        this.button_ChooseAI_Easy = new SelectModeButton("button_ChooseAI_Easy", new Image("popo"));
        this.menu_ChooseAI.addObj(this.button_ChooseAI_Easy);
        this.button_ChooseAI_Normal = new SelectModeButton("button_ChooseAI_Normal", new Image("popo"));
        this.menu_ChooseAI.addObj(this.button_ChooseAI_Normal);
        this.button_ChooseAI_Hard = new SelectModeButton("button_ChooseAI_Hard", new Image("popo"));
        this.menu_ChooseAI.addObj(this.button_ChooseAI_Hard);

        //大小
        this.button_ChooseAI_Easy.resizeTo(MenuButtonWidth, MenuButtonHeight);
        this.button_ChooseAI_Normal.resizeTo(MenuButtonWidth, MenuButtonHeight);
        this.button_ChooseAI_Hard.resizeTo(MenuButtonWidth, MenuButtonHeight);

        //位置
        this.button_ChooseAI_Easy.setPosition(-300, 0);
        this.button_ChooseAI_Normal.setPosition(0, 0);
        this.button_ChooseAI_Hard.setPosition(300, 0);

        //文字显示
        Font font1 = FontLib.GetMenuButtonFont(30);
        this.button_ChooseAI_Easy.setFont(font1);
        this.button_ChooseAI_Normal.setFont(font1);
        this.button_ChooseAI_Hard.setFont(font1);


        this.button_ChooseAI_Easy.setText("Easy");
        this.button_ChooseAI_Normal.setText("Normal");
        this.button_ChooseAI_Hard.setText("Hard");

        this.button_ChooseAI_Easy.setTextColor(new Color(212, 212, 212));
        this.button_ChooseAI_Normal.setTextColor(new Color(212, 212, 212));
        this.button_ChooseAI_Hard.setTextColor(new Color(212, 212, 212));


        this.menu_ChooseAI_animator = new Animator(0);
        this.menu_ChooseAI_alphaAnimator = new Animator(0);
        this.menu_ChooseAI.addComponent(this.menu_ChooseAI_animator);
        this.menu_ChooseAI.addComponent(this.menu_ChooseAI_alphaAnimator);

        this.menu_ChooseAI_setActive(false);

    }
    public void menu_ChooseAI_setActive(boolean flag){
        this.button_ChooseAI_Easy.setActive(flag);
        this.button_ChooseAI_Normal.setActive(flag);
        this.button_ChooseAI_Hard.setActive(flag);
    }



    public void initModeMenu() {
        this.menu_SelectMode = new OthelloObject("menu_SelectMode");
        this.addObj(this.menu_SelectMode);
        this.menu_SelectMode.setPosition(MainManuButtonsPivot);

        this.button_SelectMode_local = new SelectModeButton("menu_SelectMode_local", new Image("popo"));
        this.menu_SelectMode.addObj(this.button_SelectMode_local);
        this.button_SelectMode_online = new SelectModeButton("menu_SelectMode_online", new Image("popo"));
        this.menu_SelectMode.addObj(this.button_SelectMode_online);
        this.button_SelectMode_ai = new SelectModeButton("menu__SelectMode_ai", new Image("popo"));
        this.menu_SelectMode.addObj(this.button_SelectMode_ai);
        this.button_SelectMode_back = new NormalButton("menu_SelectMode_exitMenu");
        this.menu_SelectMode.addObj(this.button_SelectMode_back);

        //大小
        this.button_SelectMode_local.resizeTo(MenuButtonWidth, MenuButtonHeight);
        this.button_SelectMode_online.resizeTo(MenuButtonWidth, MenuButtonHeight);
        this.button_SelectMode_ai.resizeTo(MenuButtonWidth, MenuButtonHeight);

        //位置
        this.button_SelectMode_local.setPosition(200, 150);
        this.button_SelectMode_online.setPosition(500, 150);
        this.button_SelectMode_ai.setPosition(800, 150);
        this.button_SelectMode_back.setPosition(0, 400);

        //文字显示
        Font font1 = FontLib.GetMenuButtonFont(30);
        this.button_SelectMode_local.setFont(font1);
        this.button_SelectMode_online.setFont(font1);
        this.button_SelectMode_ai.setFont(font1);
        Font font2 = FontLib.GetMenuButtonFont(15);
        this.button_SelectMode_back.setFont(font2);

        this.button_SelectMode_local.setText("Local");
        this.button_SelectMode_online.setText("Online");
        this.button_SelectMode_ai.setText("Ai");
        this.button_SelectMode_back.setText("Back");

        this.button_SelectMode_local.setTextColor(new Color(212, 212, 212));
        this.button_SelectMode_online.setTextColor(new Color(212, 212, 212));
        this.button_SelectMode_ai.setTextColor(new Color(212, 212, 212));
        this.button_SelectMode_back.setTextColor(new Color(212, 212, 212));


        this.menu_SelectMode_animator = new Animator(0);
        this.menu_SelectMode_alphaAnimator = new Animator(0);
        this.menu_SelectMode.addComponent(this.menu_SelectMode_animator);
        this.menu_SelectMode.addComponent(this.menu_SelectMode_alphaAnimator);

        this.menu_SelectMode_setActive(false);

    }

    private void menu_SelectMode_popOut(double delay) {
        this.menu_SelectMode_animator.forceAppend(Animation.GetSmooth(MenuShift, 0, PopOutDuration, delay));
        this.menu_SelectMode_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_SelectMode_alphaAnimator.val(), 1, PopOutDuration, delay));
    }

    private void menu_SelectMode_popBack(double delay) {
        this.menu_SelectMode_animator.forceAppend(Animation.GetSmooth(this.menu_SelectMode_animator.val(), MenuShift, PopBackDuration, delay));
        this.menu_SelectMode_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_SelectMode_alphaAnimator.val(), 0, PopBackDuration, delay));
    }

    private void menu_SelectMode_update(double dt) {
        this.menu_SelectMode.setPosition(MainManuButtonsPivot.x + this.menu_SelectMode_animator.val(), MainManuButtonsPivot.y);
        this.menu_SelectMode.setAlpha(this.menu_SelectMode_alphaAnimator.val());
    }

    private void menu_SelectMode_setActive(boolean flag) {
        this.button_SelectMode_local.setActive(flag);
        this.button_SelectMode_online.setActive(flag);
        this.button_SelectMode_ai.setActive(flag);
        this.button_SelectMode_back.setActive(flag);
    }

    private void  setMenu_ChooseAI_update(double dt) {
        this.menu_ChooseAI.setPosition(mainApp.Width/2, this.menu_ChooseAI_animator.val());
        this.menu_ChooseAI.setAlpha(this.menu_ChooseAI_alphaAnimator.val());
    }

    private void menu_ChooseAI_popOut(double delay) {
        this.menu_ChooseAI_animator.forceAppend(Animation.GetSmooth(-MenuShift, mainApp.Height/2, PopOutDuration, delay));
        this.menu_ChooseAI_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_ChooseAI_alphaAnimator.val(), 1, PopOutDuration, delay));
    }

    private void menu_ChooseAI_popBack(double delay) {
        this.menu_ChooseAI_animator.forceAppend(Animation.GetSmooth(this.menu_ChooseAI_animator.val(), -MenuShift, PopBackDuration, delay));
        this.menu_ChooseAI_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_ChooseAI_alphaAnimator.val(), 0, PopBackDuration, delay));
    }


    public boolean ChosenLocal() {
        return this.local;
    }

    public boolean ChosenOnline() {
        return this.online;
    }

    public boolean ChosenAi() {
        return this.ai;
    }



    public void initChoosingMenu(){
        this.menu_ChooseCom = new OthelloObject("menu_ChooseCom");
        this.addObj(this.menu_ChooseCom);
        this.menu_ChooseCom.setPosition(mainApp.Width/2,mainApp.Height/2);

        this.ChooseBox = new OthelloObject("ChooseBox", new Image("ChooseCom"));
        this.ChooseBox.resizeTo(mainApp.Width/1.7, mainApp.Height/2);

        this.menu_ChooseCom.addObj(this.ChooseBox);
        this.choosingMenu = new ChoosingMenu(playerManager.getPlayersName(), "Choose User");
        this.ChooseBox.addObj(this.choosingMenu);

        //位置
        this.ChooseBox.setPosition(0, 0);
        this.choosingMenu.setPosition(0,-5);


        this.menu_ChooseCom_animator = new Animator(0);
        this.menu_ChooseCom_alphaAnimator = new Animator(0);
        this.menu_ChooseCom.addComponent(this.menu_ChooseCom_animator);
        this.menu_ChooseCom.addComponent(this.menu_ChooseCom_alphaAnimator);

        this.menu_ChooseCom_setActive(false);

    }
    private void menu_ChooseCom_setActive(boolean flag) {
        this.choosingMenu.setActive(flag);
    }

    private void  menu_ChooseCom_update(double dt) {
        this.menu_ChooseCom.setPosition(mainApp.Width/2, this.menu_ChooseCom_animator.val());
        this.menu_ChooseCom.setAlpha(this.menu_ChooseCom_alphaAnimator.val());
    }

    private void menu_ChooseCom_popOut(double delay) {
        this.menu_ChooseCom_animator.forceAppend(Animation.GetSmooth(-MenuShift, mainApp.Height/2, PopOutDuration, delay));
        this.menu_ChooseCom_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_SelectMode_alphaAnimator.val(), 1, PopOutDuration, delay));
    }

    private void menu_ChooseCom_popBack(double delay) {
        this.menu_ChooseCom_animator.forceAppend(Animation.GetSmooth(this.menu_SelectMode_animator.val(), -MenuShift, PopBackDuration, delay));
        this.menu_ChooseCom_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_SelectMode_alphaAnimator.val(), 0, PopBackDuration, delay));
    }





    private void changeMenuState(MenuState nextState){
        switch (this.menuState) {
            case Main:
                this.menu_main_popBack(PopBackDuration);
                this.menu_main_setActive(false);
                break;
            case SelectMode:
                this.menu_SelectMode_popBack(PopBackDuration);
                this.menu_SelectMode_setActive(false);
                break;
            case SelectCom:
                this.menu_ChooseCom_popBack(PopBackDuration);
                this.menu_ChooseCom_setActive(false);
            case SelectAI:
                this.menu_ChooseAI_popBack(PopBackDuration);
                this.menu_ChooseAI_setActive(false);
            case Help:
                break;
        }
        switch (nextState){
            case Main:
                this.menu_main_popOut(PopOutDuration);
                this.menu_main_setActive(true);
                break;
            case SelectMode:
                this.menu_SelectMode_popOut(PopOutDuration);
                this.menu_SelectMode_setActive(true);
                break;
            case SelectCom:
                this.menu_ChooseCom_popOut(PopOutDuration);
                this.menu_ChooseCom_setActive(true);
                break;
            case SelectAI:
                this.menu_ChooseAI_popOut(PopOutDuration);
                this.menu_ChooseAI_setActive(true);
            case Help:
                break;
        }




        this.menuState = nextState;

    }
    public int getAi(){
        return this.Level;
    }


}