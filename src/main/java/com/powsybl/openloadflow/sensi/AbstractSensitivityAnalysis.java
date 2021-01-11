/**
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.powsybl.openloadflow.sensi;

import com.powsybl.commons.PowsyblException;
import com.powsybl.iidm.network.Injection;
import com.powsybl.iidm.network.Network;
import com.powsybl.math.matrix.DenseMatrix;
import com.powsybl.math.matrix.LUDecomposition;
import com.powsybl.math.matrix.MatrixFactory;
import com.powsybl.openloadflow.equations.EquationSystem;
import com.powsybl.openloadflow.equations.JacobianMatrix;
import com.powsybl.openloadflow.equations.VoltageInitializer;

import java.util.Objects;

/**
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at rte-france.com>
 */
abstract class AbstractSensitivityAnalysis {

    protected final MatrixFactory matrixFactory;

    protected AbstractSensitivityAnalysis(MatrixFactory matrixFactory) {
        this.matrixFactory = Objects.requireNonNull(matrixFactory);
    }

    protected static Injection<?> getInjection(Network network, String injectionId) {
        Injection<?> injection = network.getGenerator(injectionId);
        if (injection == null) {
            injection = network.getLoad(injectionId);
            if (injection == null) {
                throw new PowsyblException("Injection '" + injectionId + "' not found");
            }
        }
        return injection;
    }

    protected JacobianMatrix createJacobianMatrix(EquationSystem equationSystem, VoltageInitializer voltageInitializer) {
        double[] x = equationSystem.createStateVector(voltageInitializer);
        equationSystem.updateEquations(x);
        return JacobianMatrix.create(equationSystem, matrixFactory);
    }

    protected DenseMatrix solve(DenseMatrix rhs, JacobianMatrix j) {
        try {
            LUDecomposition lu = j.decomposeLU();
            lu.solve(rhs);
        } finally {
            j.cleanLU();
        }
        return rhs; // rhs now contains state matrix
    }

    protected DenseMatrix solveTransposed(DenseMatrix rhs, JacobianMatrix j) {
        try {
            LUDecomposition lu = j.decomposeLU();
            lu.solveTransposed(rhs);
        } finally {
            j.cleanLU();
        }
        return rhs; // rhs now contains state matrix
    }
}