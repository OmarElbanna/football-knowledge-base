package matchfinder.ui;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.NodeSet;
import matchfinder.model.FootballOntology;
import matchfinder.selection.Selectable;
import matchfinder.selection.SelectionEvent;
import matchfinder.selection.SelectionListener;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.*;

public class ChoicesPanel extends JPanel implements Selectable {

	private JTree tree;

	private FootballOntology ontology;

	private HashMap clsNodeMap;

	public ChoicesPanel(FootballOntology ontology) {
		this.ontology = ontology;
		clsNodeMap = new HashMap();
		createUI();
	}

	protected void createUI() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        tree = buildTree();
        tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				fireSelectionChangedEvent();
			}
		});
		add(new JScrollPane(tree));
	}

	protected JTree buildTree()  {
		MutableTreeNode rootNode = new DefaultMutableTreeNode("Choices");
		Collection<OWLClass> mainClasses = ontology.getOntologyMainClasses();
		for (OWLClassExpression owlClassExpression : mainClasses) {
            OWLClass cls = (OWLClass) owlClassExpression;
            addClsToTree(cls, rootNode);
        }

		// get property object
//		OWLDataFactory datafactory = ontology.getOntology().getOWLOntologyManager().getOWLDataFactory();
//
//		Collection<OWLNamedIndividual> matches = ontology.getReasoner().getInstances(ontology.getMatchClass()).getFlattened();
//		for(OWLNamedIndividual match : matches){
//			String matchDate = ontology.getReasoner().getDataPropertyValues(match, datafactory.getOWLDataProperty(FootballOntology.PREFERENCES.getMatchDatePropertyName())).toString();
//			MutableTreeNode childNode = new DefaultMutableTreeNode(match);
//			clsNodeMap.put(matchDate, childNode);
//			rootNode.insert(childNode, 0);
//		}

		JTree t = new JTree(rootNode);
		t.setShowsRootHandles(true);
		t.setCellRenderer(new OWLClassTreeCellRenderer());
		return t;
	}

	protected void addClsToTree(OWLClass cls, MutableTreeNode treeNode) {
		// add class and its subclasses
		MutableTreeNode childNode = new DefaultMutableTreeNode(cls);
		clsNodeMap.put(cls, childNode);
		treeNode.insert(childNode, 0);

		NodeSet<OWLNamedIndividual> instances = ontology.getReasoner().getInstances(cls);
		for(OWLNamedIndividual instance : instances.getFlattened()){
			MutableTreeNode childNode2 = new DefaultMutableTreeNode(instance);
			clsNodeMap.put(instance, childNode2);
			childNode.insert(childNode2, 0);
		}

       // Iterator it = cls.getInferredSubclasses().iterator();
        NodeSet<OWLClass> subClasses = ontology.getReasoner().getSubClasses(cls, true);
        if (!subClasses.containsEntity(ontology.getOntology().getOWLOntologyManager().getOWLDataFactory().getOWLNothing())) {
            for(OWLClass curCls : subClasses.getFlattened()) {
                addClsToTree(curCls, childNode);
            }
        }
    }

	public static void main(String [] args) {
		ChoicesPanel panel = new ChoicesPanel(new FootballOntology());
		JFrame frm = new JFrame();
		frm.setSize(300, 400);
		frm.getContentPane().setLayout(new BorderLayout());
		frm.getContentPane().add(panel);
		frm.getContentPane().add(new JLabel("Options!"), BorderLayout.NORTH);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.show();
	}

	/////////////////////////////////////////////////////////////////////////////
	//
	// An inner class that renders tree nodes.  If the user object
	// is an OWLClass the name fragment of the class is renderered,
	// otherwise, the toString method of the user object is called.
	//
	/////////////////////////////////////////////////////////////////////////////

	public class OWLClassTreeCellRenderer extends DefaultTreeCellRenderer {

		private Icon icon;

		public OWLClassTreeCellRenderer() {
			icon = Icons.getFootballClubIcon();
		}

		public Component getTreeCellRendererComponent(JTree tree,
		                                              Object value,
		                                              boolean sel,
		                                              boolean expanded,
		                                              boolean leaf,
		                                              int row,
		                                              boolean hasFocus) {
			JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			Object obj = ((DefaultMutableTreeNode)value).getUserObject();
			if(obj instanceof OWLEntity) {
				label.setText(ontology.render((OWLEntity) obj));
			}
			else {
				label.setText(value.toString());
			}
			return label;
		}


		public Icon getOpenIcon() {
			return icon;
		}


		public Icon getClosedIcon() {
			return icon;
		}


		public Icon getLeafIcon() {
			return icon;
		}


	}



	/////////////////////////////////////////////////////////////////////////////
	//
	// Implementation of Selectable
	//
	/////////////////////////////////////////////////////////////////////////////


	public Object getSelection() {
		TreePath treePath = tree.getSelectionPath();
		if(treePath != null) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
			return treeNode.getUserObject();
		}
		return null;
	}


	public void setSelection(Object obj) {
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)clsNodeMap.get(obj);
		if(treeNode != null) {
			tree.setSelectionPath(new TreePath(treeNode.getPath()));
		}
	}

	private ArrayList selectionListeners = new ArrayList();

	public void addSelectionListener(SelectionListener lsnr) {
		selectionListeners.add(lsnr);
	}


	public void removeSelectionListener(SelectionListener lsnr) {
		selectionListeners.remove(lsnr);
	}

	protected void fireSelectionChangedEvent() {
		Iterator it = selectionListeners.iterator();
		SelectionEvent e = new SelectionEvent(this);
		while(it.hasNext()) {
			((SelectionListener)it.next()).selectionChanged(e);
		}
	}
}

