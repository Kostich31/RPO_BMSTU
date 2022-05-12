import React, { Fragment, useEffect, useState } from 'react';
const LEFT_PAGE = 'LEFT';
const RIGHT_PAGE = 'RIGHT';
const PaginationComponent = (props) => {
  const totalRecords = props.totalRecords;
  const pageNeighbours = props.pageNeighbours;
  const [currentPage, setCurrentPage] = useState(props.currentPage);
  const totalPages = Math.ceil(props.totalRecords / props.pageLimit);
  const range = (from, to, step = 1) => {
    let i = from;
    const range = [];
    while (i <= to) {
      range.push(i);
      i += step;
    }
    return range;
  };
  const fetchPageNumbers = () => {
    const totalNumbers = pageNeighbours * 2 + 3;
    const totalBlocks = totalNumbers + 2;
    if (totalPages > totalBlocks) {
      const startPage = Math.max(2, currentPage - pageNeighbours);
      const endPage = Math.min(totalPages - 1, currentPage + pageNeighbours);
      let pages = range(startPage, endPage);
      const hasLeftSpill = startPage > 2;
      const hasRightSpill = totalPages - endPage > 1;
      const spillOffset = totalNumbers - (pages.length + 1);
      switch (true) {
        case hasLeftSpill && !hasRightSpill: {
          let extraPages = range(startPage - spillOffset, startPage - 1);
          pages = [LEFT_PAGE, ...extraPages, ...pages];
          break;
        }
        case !hasLeftSpill && hasRightSpill: {
          let extraPages = range(endPage + 1, endPage + spillOffset);
          pages = [...pages, ...extraPages, RIGHT_PAGE];
          break;
        }
        case hasLeftSpill && hasRightSpill:
        default: {
          pages = [LEFT_PAGE, ...pages, RIGHT_PAGE];
          break;
        }
      }
      return [1, ...pages, totalPages];
    }
    return range(1, totalPages);
  };
  useEffect(() => {
    gotoPage(1);
  }, []);
  const gotoPage = (page) => {
    const { onPageChanged = (f) => f } = props;
    onPageChanged(page);
    setCurrentPage(page);
  };
  const handleClick = (page) => (evt) => {
    evt.preventDefault();
    gotoPage(page);
  };
  const handleMoveLeft = (e) => {
    e.preventDefault();
    gotoPage(currentPage - pageNeighbours * 2 - 1);
  };
  const handleMoveRight = (e) => {
    e.preventDefault();
    gotoPage(currentPage + pageNeighbours * 2 + 1);
  };
  if (!totalRecords || totalPages === 1) return null;
  const pages = fetchPageNumbers(totalPages);
  return (
    <Fragment>
      <nav aria-label="Pagination">
        <ul className="pagination">
          {pages.map((page, index) => {
            if (page === LEFT_PAGE)
              return (
                <li key={index} className="page-item">
                  <button className="page-link" aria-label="Previous" onClick={handleMoveLeft}>
                    <span aria-hidden="true">«</span>
                    <span className="sr-only">Предыдущая</span>
                  </button>
                </li>
              );
            if (page === RIGHT_PAGE)
              return (
                <li key={index} className="page-item">
                  <button className="page-link" aria-label="Next" onClick={handleMoveRight}>
                    <span aria-hidden="true">»</span>
                    <span className="sr-only">Следующая</span>
                  </button>
                </li>
              );
            return (
              <li key={index} className={`page-item${currentPage === page ? ' active' : ''}`}>
                <button className="page-link" onClick={handleClick(page)}>
                  {page}
                </button>
              </li>
            );
          })}
        </ul>
      </nav>
    </Fragment>
  );
};
export default PaginationComponent;
