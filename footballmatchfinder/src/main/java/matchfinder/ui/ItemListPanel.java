package matchfinder.ui;



import org.semanticweb.owlapi.model.OWLEntity;
import matchfinder.model.ChoiceModel;
import matchfinder.model.ChoiceModelChangedEvent;
import matchfinder.model.ChoiceModelListener;
import matchfinder.model.FootballOntology;
import matchfinder.selection.Selectable;
import matchfinder.selection.SelectionEvent;
import matchfinder.selection.SelectionListener;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public abstract class ItemListPanel extends JPanel implements Selectable {

	private JList list;
	private Action addAction;
	private Action removeAction;
	private String title;
	private Selectable selectable;

    private FootballOntology ontology;

	public ItemListPanel(FootballOntology ontology, String title,
						 Selectable selectable,
						 ChoiceModel choiceModel) {
		this.ontology = ontology;
        this.title = title;
		this.selectable = selectable;
		choiceModel.addChoiceModelListener(new ChoiceModelListener() {
			public void modelChanged(ChoiceModelChangedEvent e) {
				updateInterface();
			}
		});


	}

	protected void createUI() {
		addAction = getAddAction();
		removeAction = getRemoveAction();
		JPanel panel = new JPanel(new BorderLayout(7, 7));
		list = new JList(new DefaultListModel());
		list.setCellRenderer(new OWLClassListCellRenderer());
		panel.add(new JScrollPane(list));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 7));
		buttonPanel.add(new JButton(addAction));
		buttonPanel.add(new JButton(removeAction));
		panel.add(buttonPanel, BorderLayout.NORTH);
		setLayout(new BorderLayout(7, 7));
		add(panel);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title),
		                                             BorderFactory.createEmptyBorder(7, 7, 7, 7)));
		removeAction.setEnabled(false);
		addAction.setEnabled(false);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
			 if(list.getSelectedValue() != null) {
				ItemListPanel.this.removeAction.setEnabled(true);
			}
			else {
				ItemListPanel.this.removeAction.setEnabled(false);
			}
			}
		});
		selectable.addSelectionListener(new SelectionListener() {
			public void selectionChanged(SelectionEvent e) {
				if(selectable.getSelection() != null) {
					addAction.setEnabled(true);
				}
				else {
					addAction.setEnabled(false);
				}
			}
		});
	}

	protected abstract Collection getListItems();

	protected abstract Action getAddAction();

	protected abstract Action getRemoveAction();

	protected void updateInterface() {
		DefaultListModel model = (DefaultListModel)list.getModel();
		model.removeAllElements();
		Iterator it = getListItems().iterator();
		while(it.hasNext()) {
			model.addElement(it.next());
		}

	}


	// Renderer for OWLClass items

	public class OWLClassListCellRenderer extends DefaultListCellRenderer {

		private Icon icon = Icons.getFootballClubIcon();

		public Component getListCellRendererComponent(JList list,
		                                              Object value,
		                                              int index,
		                                              boolean isSelected,
		                                              boolean cellHasFocus) {

			JLabel label = (JLabel)  super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setText(ontology.render((OWLEntity) value));
			label.setIcon(icon);
			return label;
		}
	}


	// Implementation of Selectable


	public Object getSelection() {
		return list.getSelectedValue();
	}


	public void setSelection(Object obj) {

	}

	private List<SelectionListener> selectionListeners = new ArrayList<SelectionListener>();

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

