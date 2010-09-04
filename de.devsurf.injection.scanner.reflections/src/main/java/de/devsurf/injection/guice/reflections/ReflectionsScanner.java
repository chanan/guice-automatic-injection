package de.devsurf.injection.guice.reflections;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;

import org.reflections.Reflections;
import org.reflections.scanners.AbstractScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.scanner.AnnotationListener;
import de.devsurf.injection.guice.scanner.ClasspathScanner;

public class ReflectionsScanner implements ClasspathScanner {
	private LinkedList<AnnotationListener> _listeners;
	private LinkedList<Pattern> packagePatterns;
	private String[] _packages;

	@Inject
	public ReflectionsScanner(@Named("packages")String... packages) {
		_listeners = new LinkedList<AnnotationListener>();
		_packages = packages;
		this.packagePatterns = new LinkedList<Pattern>();
		for(String p : packages){
			includePackage(p);
		}
	}

	@Override
	public void addAnnotationListener(AnnotationListener listener) {
		_listeners.add(listener);
	}

	@Override
	public void excludePackage(String packageName) {
	}

	@Override
	public void includePackage(final String packageName){
		String pattern = ".*"+packageName.replace(".", "\\.")+".*";
		packagePatterns.add(Pattern.compile(pattern));
	}

	@Override
	public void removeAnnotationListener(AnnotationListener listener) {
	}

	@Override
	public void scan() throws IOException {
		Set<URL> urls = new LinkedHashSet<URL>();
		for(String p : _packages){
			urls.addAll(ClasspathHelper.getUrlsForPackagePrefix(p));
		}
		new Reflections(new ConfigurationBuilder()
				.setScanners(new AnnotationScanner())
				.filterInputsBy(new Predicate<String>() {
					@Override
					public boolean apply(String input) {
						return matches(input);
					}
				})
				.setUrls(urls)
				.useParallelExecutor());
	}
	
	private boolean matches(String name){
		for(Pattern pattern : packagePatterns){
			if(pattern.matcher(name).matches()){
				return true;
			}
		}
		return false;
	}

	private class AnnotationScanner extends AbstractScanner {
		@SuppressWarnings("unchecked")
		public void scan(final Object cls) {
			ClassFile classFile = (ClassFile)cls;
			AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)classFile.getAttribute(AnnotationsAttribute.visibleTag);
			if(annotationsAttribute == null){
				return;
			}
			
			Class<Object> objectClass;
			try {
				objectClass = (Class<Object>) Class.forName(classFile.getName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return;
			}

			Map<String, java.lang.annotation.Annotation> map = new HashMap<String, java.lang.annotation.Annotation>();
			for (Annotation annotation : annotationsAttribute.getAnnotations()) {
				Class<java.lang.annotation.Annotation> annotationClass;
				try {
					annotationClass = (Class<java.lang.annotation.Annotation>) Class.forName(annotation.getTypeName());
					map.put(annotationClass.getName(), objectClass.getAnnotation(annotationClass));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					continue;
				}
			}

			for (AnnotationListener listener : _listeners) {
				listener.found(objectClass, map);
			}
		}
	}
}
