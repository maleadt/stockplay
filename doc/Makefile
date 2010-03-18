all: clean pdf qclean

pdf: document.tex
	pdflatex document.tex
	makeindex document.idx
	pdflatex document.tex
	latex_count=5 ; \
	while egrep -s 'Rerun (LaTeX|to get cross-references right)' document.log && [ $$latex_count -gt 0 ] ;\
	    do \
	      echo "Rerunning latex...." ;\
	      pdflatex document.tex ;\
	      latex_count=`expr $$latex_count - 1` ;\
	    done

clean: qclean
	rm -f document.pdf
	rm -f *~

qclean:
	rm -f *.ps *.dvi *.aux *.toc *.idx *.ind *.ilg *.log *.out
