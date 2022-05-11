package ru.vsu.cs.zachetka_server.component;

import org.springframework.stereotype.Component;
import ru.vsu.cs.zachetka_server.model.enumerate.EvalType;
import ru.vsu.cs.zachetka_server.model.enumerate.Mark;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class MarkSetComponent {

    public Set<Mark> getMarksByEvalType(EvalType evalType) {
        if (evalType == EvalType.ZACHET) {
            return new HashSet<>(Arrays.asList(Mark.NYA,Mark.NONE, Mark.ZACH, Mark.NEZ));
        } else {
            return new HashSet<>(Arrays.asList(Mark.NYA, Mark.NONE, Mark.OTL, Mark.HOR, Mark.UDOVL, Mark.NEUD));
        }
    }
}
