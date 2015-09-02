package mash.util.process;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;

public class GetPerformerListener implements TaskListener {
	
	
	private Expression varName;

	@Override
	public void notify(DelegateTask task) {
		
		task.getExecution().setVariable(varName.getExpressionText(), task.getAssignee());
			
	}
	
public Expression getVarName() {
	return varName;
}

public void setVarName(Expression varName) {
	this.varName = varName;
}

}
