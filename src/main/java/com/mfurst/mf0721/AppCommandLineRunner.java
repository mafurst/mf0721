package com.mfurst.mf0721;

import com.mfurst.mf0721.userinterface.ToolManagementInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * This component is the command line runner. Once Spring
 * has loaded the project, this component will allow the
 * user to interact with the project through the command
 * line based on the logic below.
 *
 * The command line runner does not run in test to allow automated tests to run
 */
@Profile("!test")
@Component
public class AppCommandLineRunner implements CommandLineRunner {
    @Autowired
    private ToolManagementInterface toolManagementInterface;
    /**
     * This method is triggered after Spring initializes. It
     * is the run logic for the command line runner
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        toolManagementInterface.run(System.out, System.in, System.err);
    }
}
