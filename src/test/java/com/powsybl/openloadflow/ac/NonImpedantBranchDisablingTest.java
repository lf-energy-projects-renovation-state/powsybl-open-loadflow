/**
 * Copyright (c) 2022, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.powsybl.openloadflow.ac;

import com.powsybl.commons.reporter.Reporter;
import com.powsybl.iidm.network.Network;
import com.powsybl.loadflow.LoadFlowParameters;
import com.powsybl.math.matrix.DenseMatrixFactory;
import com.powsybl.openloadflow.OpenLoadFlowParameters;
import com.powsybl.openloadflow.ac.outerloop.AcLoadFlowContext;
import com.powsybl.openloadflow.ac.outerloop.AcLoadFlowParameters;
import com.powsybl.openloadflow.ac.outerloop.AcloadFlowEngine;
import com.powsybl.openloadflow.graph.EvenShiloachGraphDecrementalConnectivityFactory;
import com.powsybl.openloadflow.network.LfNetwork;
import com.powsybl.openloadflow.network.NameSlackBusSelector;
import com.powsybl.openloadflow.network.NodeBreakerNetworkFactory;
import com.powsybl.openloadflow.network.impl.LfNetworkLoaderImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at rte-france.com>
 */
class NonImpedantBranchDisablingTest {

    @Test
    void test() {
        Network network = NodeBreakerNetworkFactory.create();
        network.getSwitch("B3").setRetained(false);
        network.getSwitch("C").setRetained(true);
        AcLoadFlowParameters parameters = OpenLoadFlowParameters.createAcParameters(new LoadFlowParameters(),
                                                                                    new OpenLoadFlowParameters(),
                                                                                    new DenseMatrixFactory(),
                                                                                    new EvenShiloachGraphDecrementalConnectivityFactory<>(),
                                                                                    Reporter.NO_OP,
                                                                                    true,
                                                                                    false);
        parameters.getNetworkParameters().setSlackBusSelector(new NameSlackBusSelector("VL1_1"));
        LfNetwork lfNetwork = LfNetwork.load(network, new LfNetworkLoaderImpl(), parameters.getNetworkParameters()).get(0);

        try (AcLoadFlowContext context = new AcLoadFlowContext(lfNetwork, parameters)) {
            var engine = new AcloadFlowEngine(context);
            engine.run();
            assertEquals(8, context.getEquationSystem().getSortedVariablesToFind().size());
            var l1 = lfNetwork.getBranchById("L1");
            var l2 = lfNetwork.getBranchById("L2");
            assertEquals(3.01884, l1.getP1().eval(), 10e-5);
            assertEquals(3.01884, l2.getP1().eval(), 10e-5);

            lfNetwork.getBranchById("C").setDisabled(true);

            engine.run();
            assertEquals(8, context.getEquationSystem().getSortedVariablesToFind().size()); // we have kept same variables!!!
            assertEquals(0, l1.getP1().eval(), 10e-5);
            assertEquals(6.07782, l2.getP1().eval(), 10e-5);
        }
    }
}