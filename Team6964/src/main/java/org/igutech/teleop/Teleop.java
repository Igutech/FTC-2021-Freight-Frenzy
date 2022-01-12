package org.igutech.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.igutech.config.Hardware;
import org.igutech.teleop.modules.BulkRead;
import org.igutech.teleop.modules.Delivery;
import org.igutech.teleop.modules.DisconnectWorkaround;
import org.igutech.teleop.modules.DriveTrain;
import org.igutech.teleop.modules.GamepadService;
import org.igutech.teleop.modules.Intake;
import org.igutech.teleop.modules.TimerService;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TeleOp(name = "TeleOp", group = "Igutech")
public class Teleop extends OpMode {

    private static Teleop instance;

    public static Teleop getInstance() {
        return instance;
    }

    private ArrayList<Module> modules;
    private TimerService timerService;

    private Hardware hardware;
    private ElapsedTime elapsedTime;
    private int loops = 0;

    private void registerModules() {

        modules.add(new DriveTrain());
        modules.add(new Delivery());
        modules.add(new Intake());
    }

    private void registerServices() {
        modules.add(new DisconnectWorkaround());
        modules.add(new GamepadService(gamepad1, gamepad2));
        modules.add(new BulkRead());
    }

    /**
     * Get a list of all registered modules and services
     *
     * @return a list of all registered modules
     */
    public List<Module> getModules() {
        return modules;
    }

    /**
     * Register and initialize a new module
     *
     * @param module Module to initialize
     */
    public void insmod(Module module) {
        modules.add(module);
        module.init();
        sortModules();
    }

    /**
     * Get a list of all registered services
     *
     * @return A list of all registered services
     */
    public List<Service> getServices() {
        List<Service> services = new ArrayList<>();
        for (Module m : getModules()) {
            if (m instanceof Service)
                services.add((Service) m);
        }
        return services;
    }

    /**
     * Get a service by its registered name
     *
     * @param name The name of the service
     * @return The service, or null if not found
     */
    public Service getService(String name) {
        for (Service s : getServices()) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    /**
     * Sorts the list of modules/services
     */
    private void sortModules() {
        Collections.sort(modules);
    }

    /**
     * Get a module or service by its name
     *
     * @param name Name of module or service
     * @return Module or service, or null if not found
     */
    public Module getModuleByName(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    /**
     * Get the hardware object representing the robot
     *
     * @return Hardware object
     */
    public Hardware getHardware() {
        return hardware;
    }

    @Override
    public void init() {
        instance = this;
        hardware = new Hardware(hardwareMap);
        modules = new ArrayList<>();
        timerService = new TimerService();
        registerModules();
        registerServices();
        sortModules();

        for (Module m : modules) {
            if (m.isEnabled()) m.init();
        }
    }

    @Override
    public void init_loop() {
        for (Module m : modules) {
            if (m.isEnabled()) m.initLoop();
        }
    }

    @Override
    public void start() {
        for (Module m : modules) {
            if (m.isEnabled()) m.start();
        }
        timerService.start();
        elapsedTime = new ElapsedTime((ElapsedTime.Resolution.MILLISECONDS));
    }

    @Override
    public void loop() {

        try {
            long nanos = System.nanoTime();
            timerService.loop();
            for (Module m : modules) {
                if (m.isEnabled()) m.loop();
            }
//            long diff = System.nanoTime() - nanos;
//            if (diff < 5000000L)
//                Thread.sleep((int) ((5000000 - diff) / 1000000));
            loops++;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (elapsedTime.milliseconds() > 1000) {
            System.out.println("Total loop: " + loops);
            elapsedTime.reset();
            loops = 0;
        }
    }

    @Override
    public void stop() {
        for (Module m : modules) {
            if (m.isEnabled()) m.stop();
        }
    }

}
