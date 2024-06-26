/**
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * SPDX-License-Identifier: MPL-2.0
 */
package com.powsybl.openloadflow.network;

/**
 * @author Anne Tilloy {@literal <anne.tilloy at rte-france.com>}
 * @author Florian Dupuy {@literal <florian.dupuy at rte-france.com>}
 */
public class ShuntVoltageControl extends DiscreteVoltageControl<LfShunt> {

    public ShuntVoltageControl(LfBus controlledBus, int targetPriority, double targetValue, Double targetDeadband) {
        super(controlledBus, Type.SHUNT, targetPriority, targetValue, targetDeadband);
    }

    @Override
    public boolean isControllerEnabled(LfShunt controllerElement) {
        return controllerElement.isVoltageControlEnabled();
    }
}
