all: clean document.pdf

pdf: document.pdf

document.pdf: document.tex
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


clean:
	rm -f *.ps *.dvi *.aux *.toc *.idx *.ind *.ilg *.log *.out document.pdf
