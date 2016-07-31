/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.core.states;

import com.dg.core.ClientApplication;
import com.dg.core.OrbitalCamera;
import com.dg.nifty.HUDScreenController;
import com.dg.core.path.Connection;
import com.dg.core.path.StreetMesh;
import com.dg.game.units.Battalion;
import com.dg.game.units.BattalionStateEnum;
import com.dg.game.OccurenceResolver;
import com.dg.game.units.Occurrence;
import com.dg.game.units.OccurrenceStateEnum;
import com.dg.game.units.OccurrenceTypeEnum;
import com.dg.game.Player;
import static com.dg.game.units.BattalionStateEnum.COMBATING;
import static com.dg.game.units.BattalionStateEnum.DEPLOYED;
import static com.dg.game.units.BattalionStateEnum.FINISHED;
import static com.dg.game.units.BattalionStateEnum.REACHED;
import com.dg.game.units.internal.CopTypeEnum;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * O jogo por assim dizer
 *
 * @author Luís e Victor
 */
public final class GameAppState extends AbstractAppState {

    private Application application;
    private Node gameNode;
    private StreetMesh streets;
    private float lAngle;
    //light
    private PointLight lPointLight;
    private Geometry lightMdl;
    //hud controller
    private HUDScreenController hud;
    //crime bar
    private Picture crimeBar, crimeBarFill;
    private int barLevel = 0;
    //game elements
    private Occurrence selectedOcc;
    private Player player;
    private Set<Occurrence> occs;
    private Set<Battalion> bats;
    //crime rate
    private float crimerate;
    //timing
    private float timeElapsed;
    private float[][] cicleTime;
    //base node
    private com.dg.core.path.Node baseNode;
    //audio
    private AudioNode musica;

    public GameAppState() {
        //1° - occurence time | 2º - payday time | 3° delay occurrence resolve
        this.cicleTime = new float[][]{{10f, 0f}, {15f, 0f}, {2f, 0f}};
        occs = new HashSet<>();
        bats = new HashSet<>();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.application = app;

        player = new Player(app.getAssetManager());
        musica = new AudioNode(application.getAssetManager(),
                "Sounds/Musicas/CrimeRatteBaixo.ogg", false, true);
        musica.setDirectional(false);
        musica.setPositional(false);
        musica.setLooping(true);
        musica.setVolume(0.5f);
        musica.play();

        initNifty();
        initCrimeBar();

        // Forgive me father for I have sinned
        // Game Node set-up
        SimpleApplication simpleApp = (SimpleApplication) app;
        this.gameNode = new Node("gameNode");
        simpleApp.getRootNode().attachChild(this.gameNode);
        Node unitsNode = new Node("unitsNode");
        unitsNode.attachChild(new Node("cops"));
        unitsNode.attachChild(new Node("bandits"));
        this.gameNode.attachChild(unitsNode);


        // Little viewport Tweak
        app.getViewPort().setBackgroundColor(ColorRGBA.DarkGray);
        createArena();
        initializeHandling();

        InputStream inp = getClass().getResourceAsStream("/Scenes/pedro.map");
        BufferedReader in = new BufferedReader(new InputStreamReader(inp));
        String str;
        StringBuilder sb = new StringBuilder();
        try {
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }

            JSONParser jp = new JSONParser();
            JSONObject model = (JSONObject) jp.parse(sb.toString());

            JSONObject routes = new JSONObject(model);
            routes.remove("blocks");

            JSONArray blocks = (JSONArray) model.get("blocks");
            createBlocks(blocks);
            createStreets(routes);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(GameAppState.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        baseNode = streets.getBaseNode();
        Geometry base = new Geometry("Base", new Torus(10, 10, 10f, 10f));
        base.setMaterial(new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"));
        base.getMaterial().setBoolean("UseMaterialColors", true);
        base.getMaterial().setColor("Diffuse", ColorRGBA.White);
        base.getMaterial().setColor("Ambient", ColorRGBA.Green);
        base.getMaterial().setFloat("Shininess", 20f);
        base.getMaterial().setColor("Specular", ColorRGBA.Green);
        base.getMaterial().setColor("GlowColor", ColorRGBA.Green);
        base.setLocalTranslation(getPathCoordinates(baseNode.getPosition()));
        gameNode.attachChild(base);

        //input
        application.getInputManager().addMapping("Mouse Select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        application.getInputManager().addListener(mousepick, "Mouse Select");

        //colision


    }

    @Override
    public void update(float tpf) {
        if (!isEnabled()) {
            return;
        }
        gameUpdate(tpf);
        occurrenceUpdate(tpf);
        battalionUpdate(tpf);
    }

    //begin game elements updates
    
    private void gameUpdate(float tpf) {
        timeElapsed += tpf;
        for (int i = 0; i < cicleTime.length; i++) {
            cicleTime[i][1] += tpf;
        }
        if (cicleTime[0][0] < cicleTime[0][1]) {
            createOccurrence();
            cicleTime[0][1] = 0f;
        }
        if (cicleTime[1][0] < cicleTime[1][1]) {
            player.payDay(50f);
            cicleTime[1][1] = 0f;
        }
        atualizaHudPlayer();
    }

    private void occurrenceUpdate(float tpf) {
        for (Iterator<Occurrence> it = occs.iterator(); it.hasNext();) {
            Occurrence o = it.next();
            o.update(tpf);
            switch (o.getState()) {
                case OPEN:
                    //show messages
                    break;
                case FINISH_ARREST:
                    crimerate -= o.getType().getCrimeValue() - 0.5;
                    //show messages
                    it.remove();
                    break;
                case FINISH_DEAD:
                    crimerate -= o.getType().getCrimeValue() - 0.5;
                    //show messages
                    it.remove();
                    break;
                case FINISH_SCAPE:
                    //show messages
                    crimerate += o.getType().getCrimeValue();
                    it.remove();
                    break;
            }
            atualizaCrimeBar();
            if (!o.live) {
                if (o.getState() == OccurrenceStateEnum.FINISH_SCAPE) {
                    
                } else if (o.getState() == OccurrenceStateEnum.FINISH_ARREST || o.getState() == OccurrenceStateEnum.FINISH_DEAD) {
                    
                }
                if (selectedOcc != null && selectedOcc.equals(o)) {
                    hud.openOccurence(null, false);

                }
                it.remove();
            }
        }
    }

    private void battalionUpdate(float tpf) {
        for (Iterator<Battalion> it = bats.iterator(); it.hasNext();) {
            Battalion b = it.next();
            switch (b.getState()) {
                case DEPLOYED:
                    b.update(tpf);
                    break;
                case REACHED:
                    resolveOcc(b, b.destiny);
                    break;
                case COMBATING:
                    cicleTime[2][0] += tpf;
                    if (cicleTime[2][0] < cicleTime[2][1]) {
                        gameNode.detachChild(b);
                        b.setState(FINISHED);
                        AudioNode au;
                        if (b.resolver.getResult()) {
                            au = new AudioNode(application.getAssetManager(), "Sounds/Botoes/somvitoria.ogg");
                        } else {
                            au = new AudioNode(application.getAssetManager(), "Sounds/Botoes/somderrotaocorrencia.ogg");
                        }
                        au.setDirectional(false);
                        au.setPositional(false);
                        au.setLooping(true);
                        au.play();
                    }
                    break;
                case FINISHED:
                    player.addBattalionResult(b);
                    it.remove();
                    break;
            }
        }
    }

    //end
    
    @Override
    public void cleanup() {
        super.cleanup();
        musica.stop();
        this.gameNode.getParent().detachChild(this.gameNode);
        ((SimpleApplication) application).getGuiNode().detachChild(crimeBar);
        ((SimpleApplication) application).getGuiNode().detachChild(crimeBarFill);
//        System.exit(0);
    }

    public void resolveOcc(Battalion bat, Occurrence occ) {
        OccurenceResolver re = new OccurenceResolver(occ, bat);
        re.resolveOccurrence();
        bat.setState(COMBATING);
        bat.resolver = re;
    }

    public void sendBattalionToOcc(Battalion bat, Occurrence occ) {
        bat.setDestiny(occ);
        bat.setLocalTranslation(getPathCoordinates(baseNode.getPosition()));
        List<com.dg.core.path.Node> way = streets.getShortestPath(baseNode, occ.getNode());
        gameNode.attachChild(bat);
        List<Vector3f> vs = new ArrayList<>();
        for (com.dg.core.path.Node node : way) {
            vs.add(getPathCoordinates(node.getPosition()));
        }
        Logger.getGlobal().log(Level.INFO, "Rota fisica:\n{0}", vs);
        bat.moveByVec(vs);
        bats.add(bat);
        new AudioNode(application.getAssetManager(), "Sounds/Botoes/envio.ogg").play();
    }

    //begin luis
    
    public void initializeHandling() {
        Node node = new Node("chaseCam");
        this.gameNode.attachChild(node);
        node.move(0, -50f, 0);
        OrbitalCamera oc = new OrbitalCamera(this.application.getCamera(), this.gameNode.getChild("chaseCam"), this.application.getInputManager());
    }

    public void createArena() {
        Quad qd = new Quad(1000.0f, 1000.0f);
        Geometry arenaBase = new Geometry("ArenaBase", qd);
        arenaBase.rotate(-FastMath.PI / 2, 0f, 0f);
        arenaBase.center();

        Material mat = new Material(this.application.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 5);
        mat.setBoolean("UseMaterialColors", true);

        mat.setColor("Ambient", ColorRGBA.Black);
        mat.setColor("Diffuse", ColorRGBA.Gray);
        mat.setColor("Specular", ColorRGBA.Gray);
        arenaBase.setMaterial(mat);

        FilterPostProcessor fpp = new FilterPostProcessor(this.application.getAssetManager());
        fpp.addFilter(new BloomFilter(BloomFilter.GlowMode.Objects));
        this.application.getViewPort().addProcessor(fpp);

        this.gameNode.attachChild(arenaBase);

        lightMdl = new Geometry("Light", new Sphere(10, 10, 0.1f));
        lightMdl.setMaterial(this.application.getAssetManager().loadMaterial("Common/Materials/RedColor.j3m"));
        lightMdl.getMesh().setStatic();
        this.gameNode.attachChild(lightMdl);

        lPointLight = new PointLight();
        lPointLight.setColor(ColorRGBA.White);
        lPointLight.setRadius(4f);
        this.gameNode.addLight(lPointLight);

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        dl.setColor(ColorRGBA.White);
        this.gameNode.addLight(dl);

        attachCoordinateAxes(new Vector3f());
    }

    public void createStreets(JSONObject streets) {
        System.out.println(streets.toJSONString());

        this.streets = new StreetMesh(streets);
        Node n = new Node("connections");


        for (com.dg.core.path.Node q : this.streets.getNodes()) {
            Geometry geo = new Geometry("Node - " + q.getId(), new Box(1, 1, 1));
            geo.setMaterial(new Material(application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"));
            geo.getMaterial().setColor("Color", ColorRGBA.Blue);
            geo.setLocalTranslation(getPathCoordinates(q.getPosition()));
            gameNode.attachChild(geo);
        }

        Material mat = new Material(this.application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Yellow);

//        mat.getAdditionalRenderState().setPolyOffset(10f, 10f);
//        n.setMaterial(mat);

        for (Connection c : this.streets.getConnections()) {
            Line ray = new Line(getPathCoordinates(c.getA().getPosition()), getPathCoordinates(c.getB().getPosition()));
            ray.setLineWidth(2f);
//            System.out.println("" + getPathCoordinates(c.getA().getPosition()) + "" + getPathCoordinates(c.getB().getPosition()));
//            System.out.println(c.getA().getPosition().subtract(500, 500) + " - " + c.getB().getPosition().subtract(500, 500));
            Geometry geom = new Geometry("conn" + c.getId(), ray);
            geom.setMaterial(mat);
            n.attachChild(geom);
        }

//        Line ray = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 500f, 0f));
//        Geometry geom = new Geometry("ray",ray);


//        geom.setMaterial(mat);

        gameNode.attachChild(n);
    }

    public void createBlocks(JSONArray blocks) {
        Node n = new Node("buildings");
        this.gameNode.attachChild(n);

        Material mat = new Material(this.application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.selectTechnique("GBuf");
//        mat.setFloat("Shininess", 12);
//        mat.setBoolean("UseMaterialColors", true);
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.setColor("Color", new ColorRGBA(0, 0, 0, 0.5f));

//        mat.setTexture("ColorRamp", assetManager.loadTexture("Textures/ColorRamp/cloudy.png"));
//
//        mat.setBoolean("VTangent", true);
//        mat.setBoolean("Minnaert", true);
//        mat.setBoolean("WardIso", true);
//        mat.setBoolean("VertexLighting", true);
//        mat.setBoolean("LowQuality", true);
//        mat.setBoolean("HighQuality", true);
//
//        mat.setColor("Ambient",  ColorRGBA.White);
//        mat.setColor("Diffuse",  ColorRGBA.White);
//        mat.setColor("Specular", ColorRGBA.Gray);

        for (Object o : blocks) {
            JSONObject jo = (JSONObject) o;

            float x = ((Double) jo.get("x")).floatValue();
            float y = ((Double) jo.get("y")).floatValue();
            float w = ((Double) jo.get("width")).floatValue();
            float h = ((Double) jo.get("height")).floatValue();
            float c = ((Double) jo.get("color")).floatValue();

            Box box = new Box(w / 2, (c * 100) / 2, h / 2);
            Geometry g = new Geometry("building", box);
            g.center();
            g.setQueueBucket(Bucket.Transparent);

            //g.setLocalTranslation(new Vector3f(x.floatValue() / 100, 0f, y.floatValue() / 100));
            //g.scale(h.floatValue() / 100, c.floatValue() / 100, );

            Vector3f coordinates = getBuildingCoordinates(new Vector2f(x, y), new Vector2f(w, h), c);
            g.move(coordinates);
            g.setMaterial(mat);

            n.attachChild(g);
        }
    }

    private void attachCoordinateAxes(Vector3f pos) {
        Arrow arrow = new Arrow(Vector3f.UNIT_X);
        arrow.setLineWidth(4); // make arrow thicker
        putShape(arrow, ColorRGBA.Red).setLocalTranslation(pos);

        arrow = new Arrow(Vector3f.UNIT_Y);
        arrow.setLineWidth(4); // make arrow thicker
        putShape(arrow, ColorRGBA.Green).setLocalTranslation(pos);

        arrow = new Arrow(Vector3f.UNIT_Z);
        arrow.setLineWidth(4); // make arrow thicker
        putShape(arrow, ColorRGBA.Blue).setLocalTranslation(pos);
    }

    private Geometry putShape(Mesh shape, ColorRGBA color) {
        Geometry g = new Geometry("coordinate axis", shape);
        Material mat = new Material(this.application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        g.scale(100f);
        gameNode.attachChild(g);
        return g;
    }

    public Vector3f getBuildingCoordinates(Vector2f position, Vector2f size, float color) {
        return new Vector3f(position.x - 500f + (size.x / 2), (color * 100) / 2, position.y - 500f + (size.y / 2));
    }

    public Vector3f getPathCoordinates(Vector2f position) {
        return new Vector3f(position.x - 500f, 1f, position.y - 500f);
    }

    //end luis
    
    public void initNifty() {
        Nifty nifty = ((ClientApplication) this.application).getNifty();
        hud = new HUDScreenController(this);
        nifty.registerScreenController(hud);
        try {
//            nifty.validateXml("Interface/nifty/hud.xml");
            nifty.addXml("Interface/nifty/hud.xml");
            nifty.gotoScreen("hud");
        } catch (Exception ex) {
            Logger.getLogger(GameAppState.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE);
        Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE);
    }

    /**
     * Seta o nível da crime bar
     *
     * @param nivel de 0 a 10
     */
    public void setCrimeBar(int nivel) {
        if (nivel >= 0 && nivel <= 10) {
            if (barLevel != nivel) {
                atualizaMusica();
                crimeBarFill.setImage(application.getAssetManager(), "Interface/img/hud/crimebar/" + nivel + ".png", true);
                barLevel = nivel;
            }
        }
    }

    /**
     * inicia a crime bar
     */
    public void initCrimeBar() {
        crimeBar = new Picture("crime bar");
        crimeBar.setImage(application.getAssetManager(), "Interface/img/hud/conjunto3/barra-crimerate-1.png", true);
        crimeBar.setHeight(((SimpleApplication) application).getContext().getSettings().getHeight() * 2 / 3);
        crimeBar.setWidth(((SimpleApplication) application).getContext().getSettings().getWidth() / 12);
        crimeBar.setPosition(10f, ((SimpleApplication) application).getContext().getSettings().getHeight() / 4);
        ((SimpleApplication) application).getGuiNode().attachChild(crimeBar);
        crimeBarFill = new Picture("crime bar fill");
        crimeBarFill.setImage(application.getAssetManager(), "Interface/img/hud/crimebar/0.png", true);
        crimeBarFill.setHeight(((SimpleApplication) application).getContext().getSettings().getHeight() * 2 / 3);
        crimeBarFill.setWidth(((SimpleApplication) application).getContext().getSettings().getWidth() / 12);
        crimeBarFill.setPosition(10f, ((SimpleApplication) application).getContext().getSettings().getHeight() / 4);
        ((SimpleApplication) application).getGuiNode().attachChild(crimeBarFill);
    }
    
    //mouse pickup select listner
    private AnalogListener mousepick = new AnalogListener() {
        @Override
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals("Mouse Select")) {
                // Reset results list.
                CollisionResults results = new CollisionResults();
                // Convert screen click to 3d position
                Vector2f click2d = application.getInputManager().getCursorPosition();
                Vector3f click3d = application.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = application.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                // Aim the ray from the clicked spot forwards.
                Ray ray = new Ray(click3d, dir);
                // Collect intersections between ray and all nodes in results list.
                gameNode.collideWith(ray, results);
                // Use the results -- we rotate the selected geometry.
                if (results.size() > 0) {
                    // The closest result is the target that the player picked:
                    Iterator<CollisionResult> it = results.iterator();
                    Occurrence ocur = null;
                    while (it.hasNext()) {
                        Geometry target = it.next().getGeometry();
                        if (target instanceof Occurrence) {
                            ocur = (Occurrence) target;
                            break;
                        }//if instance of
                    }// while iterator
                    if (ocur != null) {
                        if (selectedOcc != ocur) {
                            for (Occurrence o : occs) {
                                o.setSelected(false);
                            }
                            selectedOcc = ocur;
                            selectedOcc.setSelected(true);
                            hud.openOccurence(selectedOcc, true);
                            Logger.getGlobal().log(Level.INFO, "Ocorencia selecionada: {0}", selectedOcc.toString());
                        }
                    } else {
                        selectedOcc = null;
                        for (Occurrence o : occs) {
                            o.setSelected(false);
                        }
                        hud.openOccurence(null, false);
                    }
                }//if results <= 0
            }
        }
    };

    private void createOccurrence() {
        Occurrence o = new Occurrence(OccurrenceTypeEnum.FACIL,
                application.getAssetManager(), streets.getRandNode());
        occs.add(o);
        gameNode.attachChild(o);
        Logger.getGlobal().log(Level.INFO, "Ocorrencia criada. {0}", cicleTime[0][1]);
    }

    private void atualizaCrimeBar() {
        setCrimeBar((int) crimerate / 10);
    }

    private void atualizaMusica() {
        musica.stop();
        if (crimerate <= 25) {
            musica = new AudioNode(application.getAssetManager(),
                    "Sounds/Musicas/CrimeRatteBaixo.ogg", false, true);
        } else if (crimerate > 90) {
            musica = new AudioNode(application.getAssetManager(),
                    "Sounds/Musicas/CrimeRatteEstupidamenteAlto.ogg", false, true);
        } else if (crimerate > 65) {
            musica = new AudioNode(application.getAssetManager(),
                    "Sounds/Musicas/CrimeRatteAlto.ogg", false, true);
        } else if (crimerate > 35) {
            musica = new AudioNode(application.getAssetManager(),
                    "Sounds/Musicas/CrimeRatteMedio.ogg", false, true);
        }
        musica.setVolume(0.5f);
        musica.setDirectional(false);
        musica.setPositional(false);
        musica.setLooping(true);
        musica.play();
    }

    public void sendBattalion(CopTypeEnum copTypeEnum) {
        Battalion bat = player.deployCopsByType(copTypeEnum, baseNode);
        if (bat != null) {
            if (bat.getUnitQuantity() > 0) {
                Logger.getGlobal().log(Level.INFO, "Enviando batalhão: {0} para ocorrencia: {1}", new Object[]{bat, selectedOcc});
                sendBattalionToOcc(bat, selectedOcc);
            }
        } else {
            //mensagem de erro
        }
    }

    public void buyCop(CopTypeEnum copTypeEnum) {
        if (player.buyCop(copTypeEnum)) {
            atualizaHudPlayer();
        } else {
            //todo message
        }
    }

    public void sellCop(CopTypeEnum copTypeEnum) {
        if (player.sellCop(copTypeEnum)) {
            atualizaHudPlayer();
        } else {
            //todo message
        }
    }

    private void atualizaHudPlayer() {
        int[] qs = player.getCopsCount();
        hud.atualizaCopCount(qs[0], qs[1], qs[2]);
        hud.atualizaGrains((int) player.getGrains());
    }

    public void quit() {
        //todo salva
        application.getStateManager().detach(this);
        ((ClientApplication) application).getNifty().gotoScreen("menu");
    }
    
    public void playBuffAudio(String path) {
        
    }
    
}
