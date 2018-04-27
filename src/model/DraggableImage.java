package model;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

public class DraggableImage extends Canvas implements DragGestureListener, DragSourceListener {

	private static final long serialVersionUID = -3232084244971439743L;
	public DragSource dragSource;
	public Image image;
	
	public DraggableImage() {
		super();
		dragSource = new DragSource();

		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
	}
	
	@Override
	public void dragDropEnd(DragSourceDropEvent event) {
		System.out.println("Drag action End");
	}

	@Override
	public void dragEnter(DragSourceDragEvent event) {
		System.out.println("Drag enter");
	}

	@Override
	public void dragExit(DragSourceEvent event) {
		System.out.println("Drag exit");
	}

	@Override
	public void dragOver(DragSourceDragEvent event) {
		System.out.println("Drag over");
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent event) {
		System.out.println("Drag action changed");
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent event) {
		Transferable transferable = new StringSelection("lol");

		dragSource.startDrag(event, DragSource.DefaultCopyDrop, transferable, this);		
	}
}
