/**
 * Copyright (c) 2021, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.powsybl.openloadflow.equations;

import com.powsybl.openloadflow.network.ElementType;
import com.powsybl.openloadflow.network.LfBus;

import java.util.Objects;

/**
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at rte-france.com>
 */
public abstract class AbstractBusEquationTerm<V extends Enum<V> & Quantity, E extends Enum<E> & Quantity> extends AbstractNamedEquationTerm<V, E> {

    protected final LfBus bus;

    protected AbstractBusEquationTerm(LfBus bus) {
        this.bus = Objects.requireNonNull(bus);
    }

    @Override
    public ElementType getElementType() {
        return ElementType.BUS;
    }

    @Override
    public int getElementNum() {
        return bus.getNum();
    }
}