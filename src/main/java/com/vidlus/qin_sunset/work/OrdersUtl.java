package com.vidlus.qin_sunset.work;

import com.vidlus.qin_sunset.core.Authed;
import com.vidlus.qin_sunset.core.Issued;
import com.vidlus.qin_sunset.core.WayToRun;
import com.vidlus.qin_sunset.swap.IssuedAnswer;
import com.vidlus.qin_sunset.swap.IssuedQuestion;

public class OrdersUtl {

    private OrdersUtl() {}

    public static String askConfig(WayToRun wayToRun, Authed authed, String name) {
        String result = null;
        if (authed != null) {
            result = authed.getConfig(name);
        }
        if (result == null) {
            result = wayToRun.airWays.setup.configMap.get(name);
        }
        if (result == null) {
            result = "";
        }
        return result;
    }

    public static IssuedAnswer askIssued(Issued issued, IssuedQuestion question) {
        var answer = new IssuedAnswer();
        if (question.askIsDone != null && question.askIsDone) {
            answer.isDone = issued.isDone();
        }
        if (question.askResultCode != null && question.askResultCode) {
            answer.resultCode = issued.getResultCode();
        }
        if (question.askFinishedAt != null && question.askFinishedAt) {
            answer.finishedAt = issued.getFinishedAt();
        }
        if (question.askCreatedAt != null && question.askCreatedAt) {
            answer.createdAt = issued.getCreatedAt();
        }
        if (question.askHasOut != null && question.askHasOut) {
            answer.hasOut = issued.hasOut();
        }
        if (question.askOutLines != null && question.askOutLines) {
            answer.outLines = issued.getOutLines();
        }
        if (question.askOutLinesFrom != null && question.askOutLinesFrom > -1) {
            answer.outLinesFrom = issued.getOutLinesFrom(question.askOutLinesFrom,
                            question.askOutLinesUntil);
        }
        if (question.askOutLinesSize != null && question.askOutLinesSize) {
            answer.outLinesSize = issued.getOutLinesSize();
        }
        if (question.askHasErr != null && question.askHasErr) {
            answer.hasErr = issued.hasErr();
        }
        if (question.askErrLines != null && question.askErrLines) {
            answer.errLines = issued.getErrLines();
        }
        if (question.askErrLinesFrom != null && question.askErrLinesFrom > -1) {
            answer.errLinesFrom = issued.getErrLinesFrom(question.askErrLinesFrom,
                            question.askErrLinesUntil);
        }
        if (question.askErrLinesSize != null && question.askErrLinesSize) {
            answer.errLinesSize = issued.getErrLinesSize();
        }
        return answer;
    }

}
