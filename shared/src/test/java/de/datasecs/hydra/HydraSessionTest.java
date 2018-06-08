package de.datasecs.hydra;

import de.datasecs.hydra.shared.handler.impl.HydraSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created with love by DataSecs on 07.06.18
 */
public class HydraSessionTest {

    private HydraSession hydraSession;

    @Test
    public void checkConnection() {
        Assert.assertTrue(hydraSession.isConnected());
    }

    @Test
    public void checkClose() {
        hydraSession.close();
        Assert.assertFalse(hydraSession.getChannel().isOpen());
    }
}