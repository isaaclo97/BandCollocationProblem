package structure;

import grafo.optilib.structure.InstanceFactory;

public class BCInstanceFactory extends InstanceFactory<BCInstance> {
    @Override
    public BCInstance readInstance(String s) {
        return new BCInstance(s);
    }
}
