package com.ng.o2d;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ng.gdxutils._Main;
import com.ng.gdxutils.services.OrientationServices;
import com.ng.o2d.component.ShapeComponent;
import com.ng.o2d.shaperenderer.ShapeRendererType;
import com.ng.o2d.system.*;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * (c) 2016 Abhishek Aryan
 *
 * @author Abhishek Aryan
 * @since 11/29/2015.
 *
 * Handle loading scene adding systems to engine and update engine.
 * 1. create object of this class.
 * 2. load scene
 * 3. add system to engine.
 * 4. call update method through your render or update method.
 */
public class SceneManager {

    private static final String TAG =  SceneManager.class.getSimpleName();
    public SceneLoader sceneLoader;
    public  String scenePath;
    private StretchViewport stretchViewport;
    private static _Main game;

    public ResourceLoader resourceManager;

    /** initialization of ResourceManager which is used when we load any scene. */

    public SceneManager(_Main game){
        this(game,new ResourceLoader(game.width,game.height));
    }

    public SceneManager(_Main game,ResourceLoader resourceLoader){
        SceneManager.game=game;
        resourceManager=resourceLoader;
        resourceManager.initAllResources();

        stretchViewport=new StretchViewport(game.width,game.height);
        sceneLoader=new SceneLoader(resourceManager);
        //addExternalMapperToRetriever();
        isLoaded=true;
    }

    /** First load scene then add systems to engine. */

    public SceneManager loadScene(String name){

        this.scenePath=name;
        sceneLoader.loadScene(scenePath,stretchViewport);

        return this;
    }

    /** System must be added after loading scene. */

    public void addSystems(EntitySystem... systems){

        Engine engine=sceneLoader.getEngine();
        for(EntitySystem entitySystem:systems)
            engine.addSystem(entitySystem);
    }

    public void addExternalMapperToRetriever(){
        ComponentRetriever.addMapper(ShapeComponent.class);
    }

    /** Different method for loading of different scene. */

    public boolean isLoaded;
    public static ShapeRendererType shapeRendererType;

    public void update(){
        //Gdx.app.log(TAG,"update scene");
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
    }

    public SceneLoader loadMainScene () {

        unloadPreviousScene();
        scenePath = SceneConstant.SceneName.MAIN_SCENE.value;
        sceneLoader.loadScene(scenePath,stretchViewport);
        //sceneLoader.getEngine().addSystem(new MainSceneManager(game));
        sceneLoader.getEngine().addSystem(new BoundsUpdater());
        sceneLoader.getEngine().addSystem(new CircularMotionSystem());
        game.setOrientation(OrientationServices.Orientation.PORTRAIT);
        return sceneLoader;
    }

    public SceneLoader loadSplashScene(){
        unloadPreviousScene();

        scenePath = SceneConstant.SceneName.MAIN_SCENE.value;
        sceneLoader.loadScene(scenePath,stretchViewport);
        return sceneLoader;
    }

    public SceneLoader loadMenuScene () {
        unloadPreviousScene();

        scenePath = SceneConstant.SceneName.MENU_SCENE.value;
        sceneLoader.loadScene(scenePath,stretchViewport);
        //sceneLoader.getEngine().addSystem(new LevelSceneManager(game));

        game.setOrientation(OrientationServices.Orientation.PORTRAIT);

        return sceneLoader;
    }

    public SceneLoader loadSettingScene(){
        unloadPreviousScene();

        scenePath = SceneConstant.SceneName.SETTING_SCENE.value;
        sceneLoader.loadScene(scenePath,stretchViewport);
        //sceneLoader.getEngine().addSystem(new SettingSceneManager(game));
        game.setOrientation(OrientationServices.Orientation.PORTRAIT);

        return sceneLoader;
    }

    public SceneLoader loadHelpScene () {
        unloadPreviousScene();

        scenePath = SceneConstant.SceneName.HELP_SCENE.value;
        sceneLoader.loadScene(scenePath,stretchViewport);

        sceneLoader.injectExternalItemType(shapeRendererType=new ShapeRendererType(stretchViewport));
        //sceneLoader.getEngine().addSystem(new HelpSceneManager(game));
        sceneLoader.getEngine().addSystem(new Box2dDebugRenderSystem(sceneLoader,stretchViewport));
        sceneLoader.getEngine().addSystem(new HudRenderSystem(sceneLoader,stretchViewport));
        sceneLoader.getEngine().addSystem(new CircularMotionSystem());

        game.setOrientation(OrientationServices.Orientation.LANDSCAPE);

        return sceneLoader;
    }

    public SceneLoader loadAboutScene () {
        unloadPreviousScene();

        scenePath = SceneConstant.SceneName.ABOUT_SCENE.value;
        sceneLoader.loadScene(scenePath,stretchViewport);

        sceneLoader.injectExternalItemType(shapeRendererType=new ShapeRendererType(stretchViewport));
        //AboutSceneManager aboutSceneManager=new AboutSceneManager(game,new ItemWrapper(sceneLoader.getRoot()));
        //sceneLoader.getEngine().addSystem(aboutSceneManager);
        //sceneLoader.getEngine().addSystem(new Box2dDebugRenderSystem(sceneLoader,scratch));
        sceneLoader.getEngine().addSystem(new HudRenderSystem(sceneLoader,stretchViewport));

        game.setOrientation(OrientationServices.Orientation.LANDSCAPE);

        return sceneLoader;
    }

    public SceneLoader loadGameOverScene () {
        unloadPreviousScene();

        scenePath = SceneConstant.SceneName.GAME_OVER_SCENE.value;
        sceneLoader.loadScene(scenePath,stretchViewport);
        //sceneLoader.getEngine().addSystem(new GameOverSceneManager(game,sceneLoader));
        sceneLoader.getEngine().addSystem(new HudRenderSystem(sceneLoader,stretchViewport));
        sceneLoader.getEngine().addSystem(new CircularMotionSystem());

        game.setOrientation(OrientationServices.Orientation.LANDSCAPE);

        return sceneLoader;
    }

    public SceneLoader loadGameScene() {
        unloadPreviousScene();

        scenePath = SceneConstant.SceneName.GAME.value;
        sceneLoader.loadScene(scenePath,stretchViewport);
        //sceneLoader.getEngine().addSystem(new GameSceneManager(sceneLoader,game));
        sceneLoader.getEngine().addSystem(new CircularMotionSystem());
        sceneLoader.getEngine().addSystem(new RotationSystem());
        sceneLoader.getEngine().addSystem(new BoundsUpdater());
        sceneLoader.getEngine().addSystem(new ExpiringSystem(sceneLoader.getEngine()));

        game.setOrientation(OrientationServices.Orientation.PORTRAIT);

        return sceneLoader;
    }


    private static void unloadPreviousScene () {

    }

    public void dispose(){
        resourceManager.dispose();
    }
}
