package com.nayragames.vis;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.kotcrab.vis.runtime.component.*;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.nayragames.gdxutils.Position;
import com.nayragames.gdxutils.Scale;
import com.nayragames.gdxutils.Size;
import com.nayragames.gdxutils.b2d.BodyEditorLoader;
import com.nayragames.vis.component.AnimationComponent;
import com.nayragames.vis.component.CollisionComponent;
import com.nayragames.vis.component.HealthComponent;

/**
 * Collection of generic method for entity creation having different types of components.
 *
 * Created by ARYAN on 12/12/2015.
 */

public class GenericEntityBuilder {

    private static final String TAG = "[" + GenericEntityBuilder.class.getSimpleName() + "]";

    /*public static Entity createLeftRightEntity(World world, float width, float height){

        Entity entity=createEntity(world, Assets.imageAsset.alien3s,Size.makeSize(width*.1f,height*.1f),Position.makePosition(width/2f, height*.9f),true, Enums.Player.NPC,180);
        entity.edit().add(new LeftRightComponent());
        entity.edit().add(EntityManager.createFSMC(entity));
        entity.edit().add(new BulletSpawnComponent());

        return entity;
    }

    public static Entity createEntity(World world, TextureRegion texture, Size size, Position position, boolean isCentric, Enums.Player playerTag, Enums.Group groupTag, float angle){

        Entity entity=createEntity(world, texture, size, position, isCentric,playerTag,angle);
        world.getSystem(GroupManager.class).add(entity, groupTag.value);
        return entity;
    }

    public static Entity createEntity(World world, TextureRegion texture, Size size, Position position, boolean isCentric, Enums.Player playerTag, float angle){

        Entity entity=createEntity(world, texture, size, position, isCentric,angle);
        world.getSystem(PlayerManager.class).setPlayer(entity, playerTag.value);
        return entity;
    }

    public static Entity createEntity(World world,TextureRegion texture,Size size,Position position,boolean isCentric,float angle){

        Entity entity=createEntity(world, Enums.Layer.NP_LAYER.value,0,texture,size,position,isCentric,angle);

        return entity;
    }*/

    /*public static Entity createEntity(World world,int layer,int renderComponent,TextureRegion texture,Size size,Position position,boolean isCentric,float angle){

        Entity entity =world.createEntity();
        entity.edit().add(new Layer(layer));
        entity.edit().add(new Renderable(renderComponent));
        entity.edit().add(addVisSprite(texture,size));
        //,position,isCentric,angle

        return entity;
    }*/

    //public static void createTweenEntity(World world, int count, Main game){

        /*Entity e=createEntity(world,Assets.imageAsset.alien3s,Size.makeSize(1,1),Position.makePosition(3,3),true, Player.PLAYER,0);
        VisSprite spriteComponent=e.getComponent(VisSprite.class);

        Tween.to(spriteComponent, TransformAccessor.POS_XY,1).target(count,1).ease(TweenEquations.easeOutBack).start(game.tweenManager);
        Tween.to(spriteComponent, TransformAccessor.TINT,1).target(1,0,1).repeatYoyo(Tween.INFINITY,0).start(game.tweenManager);
*/
      //  count++;
    //}

    /*public static Entity createAnimation(World world, TextureRegion[] regions, Size size, Position origin){

        Entity entity=world.createEntity();
        entity.edit().add(new Layer(0));
        entity.edit().add(new Renderable(1));

        AnimationComponent animation=new AnimationComponent(regions,.1f, Animation.PlayMode.LOOP);
        BasicComponent basicComponent=new BasicComponent();

        basicComponent.setScaleX(4);
        basicComponent.setScaleY(4);
        basicComponent.setRotation(90);
        entity.edit().add(basicComponent);

        return entity;
    }*/

   // GenericEntityBuilder.createAnimation(world,Assets.animationAsset.blast1, Size.makeSize(1,1), Position.makePosition(1,1));

    public static Entity createAnimation(World world, TextureRegion[] regions, Enums.Layer layer, Size size, Position origin, float angle){

        Entity entity=world.createEntity();
        entity.edit().add(new Layer(layer.value));
        entity.edit().add(new Renderable(1));

        entity.edit().add(new AnimationComponent(regions,.1f, Animation.PlayMode.LOOP));
        EntityManager.addTransform(entity,origin, Scale.makeUnScale(),angle);

        return entity;
    }

    /*public static Entity createLeaderEntity(World world, float width, float height){

        Entity entity =world.createEntity();
        entity.edit().add(new Layer(Enums.Layer.PLAYER_LAYER.value));
        entity.edit().add(new Renderable(0));
        //entity.edit().add(addVisSprite(Assets.imageAsset.alien3s,Size.makeSize(width*.075f,height*.075f),Position.makePosition(width/2f, height*.9f),true,180));
        entity.edit().add(createAC(Assets.animationAsset.dangerFlipY,.1f, Animation.PlayMode.LOOP_PINGPONG));
        entity.edit().add(new BasicComponent(Size.makeSize(width*.075f,height*.075f),Position.makePosition(width/2f, height*.9f),180));



        world.getSystem(PlayerManager.class).setPlayer(entity,  Player.NPC.value);
        world.getSystem(GroupManager.class).add(entity, Enums.Group.ENEMY_SHIP.value);

        entity.edit().add(EntityManager.createSteerC(entity));
        entity.edit().add(new LeaderComponent(entity.getComponent(SteerableComponent.class), LeaderComponent.FormationType.DEFENSIVE_CIRCLE,.25f));
        entity.edit().add(new LeftRightComponent(1,.01f));
        entity.edit().add(new CollisionComponent());

        entity.edit().add(new BulletSpawnComponent(false, BulletSpawnComponent.BulletSpawnType.CIRCULAR));

        //entity.edit().add(EntityManager.createFSMC(entity));
        //entity.getComponent(FSMComponent.class).getStateMachine().changeState(EntityState.MOVE_DOWN);

        HealthComponent health=new HealthComponent();
        entity.edit().add(health);

        float percent=(health.health/health.maximumHealth)*100;
        VisText textComponent=new VisText(Assets.bitmapFontAsset.bitmapFont,String.valueOf((int)percent));
        //textComponent.setScale(.5f, .5f);
        entity.edit().add(textComponent);

        return entity;
    }*/


    public static Entity createFormationMemberEntity(World world, TextureRegion[] textureRegions, float width, float height, Entity leader){

        Entity entity =world.createEntity();
        entity.edit().add(new Layer(Enums.Layer.PLAYER_LAYER.value));
        entity.edit().add(new Renderable(0));
        //entity.edit().add(addVisSprite(Assets.imageAsset.circularBullet,Size.makeSize(width*.075f,height*.075f),Position.makePosition(1, 1),true,0));
        entity.edit().add(new AnimationComponent(textureRegions,.1f, Animation.PlayMode.LOOP));

        EntityManager.addTransform(entity, Position.makePosition(MathUtils.random(0,4.8f), 4),Scale.makeUnScale(),0);
        world.getSystem(PlayerManager.class).setPlayer(entity, Enums.Player.NPC.value);
        world.getSystem(GroupManager.class).add(entity, Enums.Group.ENEMY_SHIP.value);

        entity.edit().add(EntityManager.createSteerC(entity,true));
        //entity.edit().add(new BulletSpawnComponent(false, BulletSpawnComponent.BulletSpawnType.SINGLE));

        EntityManager.createFormationMemberComponent(entity,leader);
        entity.edit().add(EntityManager.createFSMC(entity));
        entity.edit().add(new CollisionComponent());

        HealthComponent health=new HealthComponent(5);
        entity.edit().add(health);

        float percent=(health.health/health.maximumHealth)*100;
        //VisText textComponent=new VisText(Assets.bitmapFontAsset.bitmapFont,String.valueOf((int)percent));
        //textComponent.setScale(.5f, .5f);

        //entity.edit().add(textComponent);

        return entity;
    }


    public static Entity createParticle(World world,float x,float y,float size,String fileName){

        Entity entity=world.createEntity();
        entity.edit().add(new Layer(Enums.Layer.PLAYER_LAYER.value));
        entity.edit().add(new Renderable(0));

        ParticleEffect particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("particle/"+fileName), Gdx.files.internal("particle"));

        VisParticle pc = new VisParticle(particleEffect);
        entity.edit().add(pc);
        pc.getEffect().scaleEffect(size);
        pc.getEffect().setPosition(x, y);
        return entity;
    }

    /*public static void createAnimationEntity(World world){

		*//*Entity leader =world.createEntity();
		leader.edit().add(new LayerComponent(Layer.GAME_LAYER.value));
		leader.edit().add(new RenderableComponent(0));
		leader.edit().add(addVisSprite(Resource.PLAYER_BULLET,Size.makeSize(size,size),point,true,0));*//*
    }*/

    public static Entity createPhysicsBoundary(World world, Size size, Position position, boolean isCentric, float angle){

        Entity entity=world.createEntity();
        entity.edit().add(new Renderable(0));
        entity.edit().add(new Layer(0));

        com.badlogic.gdx.physics.box2d.World physicsWorld=world.getSystem(PhysicsSystem.class).getPhysicsWorld();

        BodyDef bodyDef=new BodyDef();
        bodyDef.position.set(position.x, position.y);
        bodyDef.type= BodyDef.BodyType.StaticBody;

		/*PolygonShape polygonShape=new PolygonShape();
		polygonShape.setAsBox(sc.getWidth(), sc.getHeight());*/

        ChainShape chainShape=new ChainShape();
        chainShape.createChain(new Vector2[]{new Vector2(-size.x/2, -size.y/2),new Vector2(size.x/2, -size.y/2),new Vector2(size.x/2, size.y/2)});

        FixtureDef fixture=new FixtureDef();
        fixture.shape=chainShape;
        fixture.restitution=0;

        Body body=physicsWorld.createBody(bodyDef);
        body.setUserData(entity);

        body.createFixture(fixture);

        PhysicsBody physicsBody=new PhysicsBody(body);
        entity.edit().add(physicsBody);

        EntityManager.addOrigin(entity,size.x/2,size.y/2);

        return entity;
    }

    public static VisMusic createMusicEntity(World world,Music music){
        Entity entity=world.createEntity();
        VisMusic musicComponent=new VisMusic(music);
        musicComponent.setLooping(true);
        //musicComponent.play();
        entity.edit().add(musicComponent);
        return musicComponent;
    }

    public static Entity createSprite(World world, int layerId, TextureRegion textureRegion, Size size, Position point, float angle){

         /*VisSprite,Transform,Origin,Tint,VisID,Layer,Renderable*/

        Entity entity=world.createEntity();
        EntityManager.addVisSprite(entity,textureRegion,size);
        EntityManager.addTransform(entity,point, Scale.makeUnScale(),angle);
        EntityManager.addOrigin(entity,size.x/2,size.y/2);
        EntityManager.addTint(entity);
        //EntityManager.addVisID(entity,"");
        EntityManager.addLayer(entity,layerId);
        EntityManager.addRenderer(entity,0);

        return entity;
    }

    public static Entity createText(World world, int layerId , BitmapFont font, String text, Position point, float angle){

         /* VisText,Transform,Origin,Tint,VisID,Layer,Renderable*/

        Entity entity=world.createEntity();
        VisText visText=EntityManager.addVisText(entity,font,text);
        EntityManager.addTransform(entity,point, Scale.makeUnScale(),angle);
        EntityManager.addOrigin(entity,visText.getWidth()/2,visText.getHeight()/2);
        EntityManager.addTint(entity);
        //EntityManager.addVisID(entity,"");
        EntityManager.addLayer(entity,layerId);
        EntityManager.addRenderer(entity,0);

        return entity;
    }

    public static Entity createPhysicsSprite(World world, int layerId, TextureRegion textureRegion, Size size, Position point, float angle, BodyDef.BodyType type, float density){

        //PhysicsProperties,VisPolygon,VisSprite,PhysicsBody,PhysicsSprite,Transform,Origin,Tint,VisID,Layer,Renderer

        Entity entity=world.createEntity();
        EntityManager.addVisSprite(entity,textureRegion,size);
        EntityManager.addTransform(entity,point, Scale.makeUnScale(),angle);
        EntityManager.addOrigin(entity,0,0);
        EntityManager.addTint(entity);
        //EntityManager.addVisID(entity,"");
        EntityManager.addLayer(entity,layerId);
        EntityManager.addRenderer(entity,0);
        //entity.edit().add(new PhysicsOrigin());

      //  EntityManager.addPhysicsBody(entity,type,density,point.x,point.y,size.x,size.y,true,angle);
       // entity.edit().add(new PhysicsSprite());

        return entity;
    }

    public static Entity createPhysicsSprite(World world, int layerId, TextureRegion textureRegion, Size size, Position point, float angle, BodyDef.BodyType type, float density, String name, BodyEditorLoader loader, float scaleFactor){

        //PhysicsProperties,VisPolygon,VisSprite,PhysicsBody,PhysicsSprite,Transform,Origin,Tint,VisID,Layer,Renderer

        Entity entity=world.createEntity();
        EntityManager.addVisSprite(entity,textureRegion,size);
        EntityManager.addTransform(entity,point, Scale.makeScale(scaleFactor,scaleFactor),angle);
        EntityManager.addOrigin(entity,size.x/2,size.y/2);
        EntityManager.addTint(entity);
        //EntityManager.addVisID(entity,"");
        EntityManager.addLayer(entity,layerId);
        EntityManager.addRenderer(entity,0);

       // EntityManager.addPhysicsBody(entity,type,density,point.x,point.y,size.x,size.y,true,angle,name,loader,scaleFactor);
       // Variables variables=EntityManager.addVariable(entity);
       // variables.putInt("phy",0);
        //entity.edit().add(new PhysicsSprite());

        return entity;
    }

    public static Entity createAnimationEntity(World world, int layerId, TextureRegion[] textureRegion, Size size, Position point, float angle){

        Entity entity=world.createEntity();
        EntityManager.addAnimation(entity,textureRegion,size);
        EntityManager.addTransform(entity,point, Scale.makeUnScale(),angle);
        EntityManager.addOrigin(entity,0,0);
        EntityManager.addTint(entity);
        //EntityManager.addVisID(entity,"");
        EntityManager.addLayer(entity,layerId);
        EntityManager.addRenderer(entity,0);
        //entity.edit().add(new PhysicsOrigin());

        return entity;
    }

    public static Entity createPhysicsAnimation(World world, int layerId, TextureRegion[] textureRegion, Size size, Position point, float angle, BodyDef.BodyType type, float density){

        Entity entity=world.createEntity();
        EntityManager.addAnimation(entity,textureRegion,size);
        EntityManager.addTransform(entity,point, Scale.makeUnScale(),angle);
        EntityManager.addOrigin(entity,0,0);
        EntityManager.addTint(entity);
        //EntityManager.addVisID(entity,"");
        EntityManager.addLayer(entity,layerId);
        EntityManager.addRenderer(entity,0);
        //new PhysicsOrigin());
        //EntityManager.addPhysicsBody(entity,type,density,point.x,point.y,size.x,size.y,true,angle);
        //Variables variables=EntityManager.addVariable(entity);
        //variables.putInt("phy",0);

        return entity;
    }

    public static Entity createShape(World world, int layerId, Color color, Size size, Position point , float angle){

        Entity entity=world.createEntity();
        EntityManager.addShape(entity,size);

        if(point.isCentric) {
            EntityManager.addTransform(entity, point.x - size.x / 2f, point.y - size.y / 2f, Scale.makeUnScale(), angle);
        }
        else {
            EntityManager.addTransform(entity, point, Scale.makeUnScale(), angle);
        }

        EntityManager.addOrigin(entity,size.x/2f,size.y/2f);
        EntityManager.addTint(entity,color);
        //EntityManager.addVisID(entity,""));
        EntityManager.addLayer(entity,layerId);
        EntityManager.addRenderer(entity,0);

        return entity;
    }

    public static Entity createShape(World world, int layerId, Color color, float radius, Position point, float angle){

        Entity entity=world.createEntity();
        EntityManager.addShape(entity,radius);

        if(point.isCentric)
        EntityManager.addTransform(entity,point.x-radius/2,point.y-radius/2, Scale.makeUnScale(),angle);
        else
        EntityManager.addTransform(entity,point.x,point.y, Scale.makeUnScale(),angle);
        EntityManager.addOrigin(entity,radius/2,radius/2);
        EntityManager.addTint(entity,color);
        //EntityManager.addVisID(entity,""));
        EntityManager.addLayer(entity,layerId);
        EntityManager.addRenderer(entity,0);

        return entity;
    }

    public static Entity createPhysicsShape(World world, int layerId, Color color, Size size, Position point, float angle, BodyDef.BodyType type, float density){

        Entity entity=world.createEntity();
        EntityManager.addShape(entity,size);
        EntityManager.addTransform(entity,point, Scale.makeUnScale(),angle);
        EntityManager.addOrigin(entity,size.x/2,size.y/2);
        EntityManager.addTint(entity,color);
        //EntityManager.addVisID(entity,"");
        EntityManager.addLayer(entity,layerId);
        EntityManager.addRenderer(entity,0);
        //new PhysicsOrigin());
       // EntityManager.addPhysicsBody(entity,type,density,point.x,point.y,size.x,size.y,false,angle);

        Variables variables=EntityManager.addVariable(entity);
        variables.putInt("phy",0);

        return entity;
    }

    public static class BodyProperties {

        BodyDef.BodyType type;
        float density;
        float friction;

       BodyProperties (BodyDef.BodyType type, float density, float friction){
        this.type=type;
        this.density=density;
        this.friction=friction;

       }
    }

    public static BodyProperties getBodyProperties(BodyDef.BodyType bodyType, float density, float friction){

        return new BodyProperties(bodyType,density,friction);
    }

    public static void addPointEntity(World world){

        //Transform,VisID,Point

        Entity entity =world.createEntity();
        EntityManager.addTransform(entity, Position.makePosition(1,1), Scale.makeUnScale(),0);
        EntityManager.addVisID(entity,"");
        //EntityManager.addPoint();
    }
}
