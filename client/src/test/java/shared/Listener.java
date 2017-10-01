package shared;

import de.datasec.hydra.shared.handler.HydraSession;

/**
 * Created by DataSec on 30.09.2017.
 */
public abstract class Listener {

    public abstract void onSimplePacket(SimplePacket simplePacket, HydraSession session);
}
