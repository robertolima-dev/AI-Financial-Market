import React from 'react'

class WrapperAbsolute extends React.Component {
    constructor(props) {
        super(props);
        this.newProps = {...this.props};
        delete this.newProps.className;
    }

    render() {
        return(
          <div {...this.newProps} className={"full-absolute-wrapper " + this.props.className}>
              {this.props.children}
          </div>
        );
    }
}

export default WrapperAbsolute;